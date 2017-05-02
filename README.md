# Get worked

In root of checked out repo use following commands:

```
mvn -pl jaxb-serializer-bindings install
mvn -pl jaxb-serializer-impl package exec:java
```
Or, to run standalone .jar:
```
mvn -pl jaxb-serializer-bindings install
mvn -pl jaxb-serializer-impl package 
cd jaxb-serializer-impl/target
java -jar ./jaxb-serializer-impl-1.0-SNAPSHOT-jar-with-dependencies.jar -in ../sample/in.xml -out ./out.xml
```

# JAXB practical task

Practical task description:

Create maven project jaxb-serializer that consists of 2 sub-modules:
* jaxb-serializer-bindings

Contains XSD schema that describes the structure of data intended to store catalog of products.
XSD schema should contain following fields:
Root element is products (`<xs:element name="products">`) – complex type that contains unbounded amount of product elements (`<xs:element minOccurs="1" name="product" maxOccurs="unbounded">`).
Each of product elements consists of a set of tags: 
- name (xs:string)
- price (xs:integer)
- amount (xs:integer)
- description (xs:string)
- type (element of type ProductType – enumeration, supported values convenience, shopping, speciality, unsought)

This module should generate Java classes based on XSD schema (use JAXB maven plugin).
* jaxb-serializer-impl

Contains functionality that provides an ability to marshal and unmarshall XML files and map them to created XSD schema.

Please note, this module must contain jaxb-bindings module in a list of dependencies.
Create Main class with method main that receives from console following parameters
- in (path to input XML file, URL)
- out (path to output XML file, URL)

and unmarshalls XML file (–in parameter), creates an Java Object and print in console list of products and marshalls created object and creates xml file in the location provided by -out parameter.

Module should be packed as runnable JAR (MANIFEST.MF should contain Main-Class: `com.epam.training.Main`)
