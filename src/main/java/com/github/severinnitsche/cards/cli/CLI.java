package com.github.severinnitsche.cards.cli;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CLI {

  static {
    try {
      // Load xml and schema
      URL xml = CLI.class.getResource("/config.xml");
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(CLI.class.getResource("/AdaptableCliConfigSchema.xsd"));

      // Create JAXB-Context
      JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      unmarshaller.setSchema(schema);

      // Analyze xml
      CLI.configuration = (Configuration) unmarshaller.unmarshal(xml);
    } catch (JAXBException | SAXException e) {
      System.err.println("XML-Validation failed");
      throw new RuntimeException(e);
    }
  }

  private static Configuration configuration;

  public static void main(String[] args) {
    Register adoptable = null;
    Map<String, Object> options = null;
    for (int i = 0; i < args.length; i++) {
      String simpleArg = args[i].replaceAll("-","");
      if (adoptable == null) {
        if (simpleArg.equalsIgnoreCase("h")
            || simpleArg.equalsIgnoreCase("help")) {
          help();
        } else {
          Optional<Register> verb = configuration.adoptables.stream()
              .filter(candidate ->
                  candidate.verb.equalsIgnoreCase(simpleArg))
              .findAny();
          if (verb.isEmpty()) {
            System.err.println("Could not find verb: " + args[i]);
            System.exit(42);
          }
          adoptable = verb.get();
          options = HashMap.newHashMap(adoptable.options.size());
        }
      } else {
        if (simpleArg.equalsIgnoreCase("h")
            || simpleArg.equalsIgnoreCase("help")) {
          help(adoptable);
        }
        Optional<Option> option = adoptable.options.stream()
            .filter(candidate ->
                candidate.longName.equalsIgnoreCase(simpleArg) || candidate.shortName.equalsIgnoreCase(simpleArg))
            .findAny();
        if (option.isEmpty()) {
          System.err.println("Could not find option: "+args[i]);
          System.exit(42);
        }
        switch (option.get().type.toLowerCase()) {
          case "int", "integer" -> options.put(option.get().longName, Integer.parseInt(args[++i]));
          case "long" -> options.put(option.get().longName, Long.parseLong(args[++i]));
          case "float", "binary32" -> options.put(option.get().longName, Float.parseFloat(args[++i]));
          case "double", "binary64" -> options.put(option.get().longName, Double.parseDouble(args[++i]));
          case "short" -> options.put(option.get().longName, Short.parseShort(args[++i]));
          case "char" -> options.put(option.get().longName, args[++i].charAt(0));
          case "boolean", "bool" -> options.put(option.get().longName, Boolean.parseBoolean(args[++i]));
          case "string", "utf-16" -> options.put(option.get().longName, args[++i]);
          case "utf-8" -> options.put(option.get().longName, new String(args[++i].getBytes(), StandardCharsets.UTF_8));
        }
      }
    }
    if (adoptable == null) {
      help();
      return;
    }
    try {
      Class<?> clazz = Class.forName(adoptable.className);
      Class<?>[] parameterTypes = adoptable.options.stream().map(option ->
          switch (option.type) {
            case "int", "integer" -> int.class;
            case "long" -> long.class;
            case "float", "binary32" -> float.class;
            case "double", "binary64" -> double.class;
            case "short" -> short.class;
            case "char" -> char.class;
            case "boolean", "bool" -> boolean.class;
            case "string", "utf-16", "utf-8" -> String.class;
            default -> throw new IllegalArgumentException("Unknown Type: "+option.type);
          }
      ).toArray(Class[]::new);
      Method method = clazz.getDeclaredMethod(Adoptable.METHOD_NAME, parameterTypes);
      method.setAccessible(true);
      Map<String, Object> finalOptions = options;
      Object[] methodArgs = adoptable.options.stream().map(option ->
          finalOptions.getOrDefault(option.longName, switch (option.type) {
            case "int", "integer" -> Integer.parseInt(option.defaultValue);
            case "long" -> Long.parseLong(option.defaultValue);
            case "float", "binary32" -> Float.parseFloat(option.defaultValue);
            case "double", "binary64" -> Double.parseDouble(option.defaultValue);
            case "short" -> Short.parseShort(option.defaultValue);
            case "char" -> option.defaultValue.charAt(0);
            case "boolean", "bool" -> Boolean.parseBoolean(option.defaultValue);
            case "string", "utf-16" -> option.defaultValue;
            case "utf-8" -> new String(option.defaultValue.getBytes());
            default -> throw new IllegalArgumentException("Unknown Type: "+option.type);
          })).toArray();
      method.invoke(null, methodArgs);
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Display a help screen and exit 👩🏽‍❤️‍👨🏼
   */
  public static void help() {
    System.out.println("""
        Mau Mau
        Copyright (C) 2023 Severin Leonard Christian Nitsche
        """);
    int verbLength = configuration.adoptables.stream()
        .mapToInt(adoptable -> adoptable.verb.length()).max().orElse(10);
    for (Register adoptable : configuration.adoptables) {
      System.out.printf("  %s%s  %s%n",
          adoptable.verb,
          " ".repeat(verbLength - adoptable.verb.length()),
          adoptable.description);
    }
    System.exit(69);
  }

  /**
   * Display a help screen and exit 👩🏽‍❤️‍👨🏼
   */
  public static void help(Register adoptable) {
    System.out.printf("""
        Mau Mau
        Copyright (C) 2023 Severin Leonard Christian Nitsche
        
        %s - %s
        
        """, adoptable.verb, adoptable.description);
    int optionLength = adoptable.options.stream()
        .mapToInt(option -> option.shortName == null ? 0 : option.shortName.length() + option.longName.length())
        .max().orElse(10);
    for (Option option : adoptable.options) {
      if (option.defaultValue == null) {
        if (option.shortName == null || option.shortName.equals("")) {
          System.out.printf("  --%s   %s  %s%n",
              option.longName,
              " ".repeat(optionLength - option.longName.length()),
              option.helpText);
        } else {
          System.out.printf("  -%s, --%s%s  %s%n",
              option.shortName,
              option.longName,
              " ".repeat(optionLength - option.longName.length() - option.shortName.length()),
              option.helpText);
        }
      } else {
        if (option.shortName == null || option.shortName.equals("")) {
          System.out.printf("  --%s   %s  (Default: %s) %s%n",
              option.longName,
              " ".repeat(optionLength - option.longName.length()),
              option.defaultValue,
              option.helpText);
        } else {
          System.out.printf("  -%s, --%s%s  (Default: %s) %s%n",
              option.shortName,
              option.longName,
              " ".repeat(optionLength - option.longName.length() - option.shortName.length()),
              option.defaultValue,
              option.helpText);
        }
      }
    }
    try (var stream = Objects.requireNonNull(CLI.class.getResource(adoptable.helpFile)).openStream()) {
      System.out.println();
      System.out.println(new String(stream.readAllBytes()));
    } catch (Exception ignored) {}
    System.exit(69);
  }

}
