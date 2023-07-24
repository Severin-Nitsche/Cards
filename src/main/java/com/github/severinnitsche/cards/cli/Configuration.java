package com.github.severinnitsche.cards.cli;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Disclaimer: This Java class was generated with the assistance of an AI-powered chatbot
 * and is not the original work of the author. The classes are designed to reflect the structure of the XML
 * configuration data and have been automatically generated based on the provided XML schema.
 */

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

  @XmlElementWrapper(name = "adoptables")
  @XmlElement(name = "register")
  public List<Register> adoptables;
}
