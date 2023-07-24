package com.github.severinnitsche.cards.cli;

import jakarta.xml.bind.annotation.*;

/**
 * Disclaimer: This Java class was generated with the assistance of an AI-powered chatbot
 * and is not the original work of the author. The classes are designed to reflect the structure of the XML
 * configuration data and have been automatically generated based on the provided XML schema.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Option {

  @XmlElement(name = "short")
  public String shortName;

  @XmlElement(name = "long")
  public String longName;

  @XmlElement(name = "type")
  public String type;

  @XmlElement(name = "default")
  public String defaultValue;

  @XmlElement(name = "help")
  public String helpText;
}
