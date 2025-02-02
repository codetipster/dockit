# JavaCodeParser

# Class: JavaCodeParser

## Package: com.dockit.core.parser

The `JavaCodeParser` class is a crucial component of the `com.dockit.core.parser` package. Its primary responsibility is to parse Java code, breaking it down into understandable and manageable elements for further processing or analysis.

## Purpose and Responsibilities

The `JavaCodeParser` is designed to interpret Java code and convert it into a structured format that can be easily manipulated and analyzed by other components in the system. It takes raw Java code as input and outputs a parsed representation, typically in the form of a syntax tree or similar data structure.

This class is responsible for:

1. Reading and interpreting Java code.
2. Breaking down the code into its constituent elements (e.g., classes, methods, variables).
3. Constructing a structured representation of the code.
4. Handling parsing errors and exceptions.

## Role in the Codebase

In the overall codebase, `JavaCodeParser` serves as a bridge between raw Java code and the system's code analysis and manipulation components. By converting code into a structured format, it enables other parts of the system to work with the code in a more abstract and high-level manner.

## Design Patterns and Principles

The `JavaCodeParser` class follows the Interpreter design pattern, a behavioral pattern that provides a way to evaluate language grammar or expression. This pattern involves implementing an expression interface which tells to interpret a particular context. This pattern is used to deal with grammar in a language for which it is required to interpret.

## Key Relationships

The `JavaCodeParser` class interacts closely with several other components:

- **Code Reader**: This component provides the raw Java code that `JavaCodeParser` will parse.
- **Syntax Tree (or similar data structure)**: `JavaCodeParser` outputs its results to this data structure, which other components can then use.
- **Code Analysis Components**: These components take the output of `JavaCodeParser` and perform various analyses on the code.

## Usage

To use the `JavaCodeParser` class, provide it with raw Java code (typically as a string or a stream). The class will parse the code and output a structured representation. Handle any parsing errors or exceptions as appropriate for your application.

Please refer to the specific method documentation for more details on how to use this class effectively.

## Methods

### parse

---
## Class: JavaCodeParser
### Method: parse

**Description:**

The `parse` method is designed to analyze and extract information from a given Java source code string. It parses the input source code and generates a `ClassInfo` object that contains detailed information about the class defined in the source code.

**Parameters:**

- `sourceCode` (String): This is a string representation of the Java source code that needs to be parsed. It should contain a valid Java class definition.

**Return Value:**

- Returns a `ClassInfo` object if the parsing is successful. The `ClassInfo` object contains detailed information about the class defined in the source code. This includes data such as class name, methods, fields, annotations, and more.

- Returns `null` if the parsing fails, such as when the input string does not contain a valid Java class definition.

**Usage Example:**

```java
JavaCodeParser parser = new JavaCodeParser();
String sourceCode = "public class HelloWorld { public static void main(String[] args) { System.out.println(\"Hello, World!\"); } }";
ClassInfo classInfo = parser.parse(sourceCode);
if (classInfo != null) {
    System.out.println("Class Name: " + classInfo.getClassName());
    // Further processing...
} else {
    System.out.println("Parsing failed.");
}
```

**Important Notes:**

- The `parse` method does not support parsing of multiple class definitions in a single source code string. If the input string contains multiple class definitions, only the first one will be parsed.

- The method does not perform any syntax checking or error correction. If the input string contains syntax errors, the method will fail and return `null`.

- The method does not support parsing of nested or inner classes. If the input string contains nested or inner class definitions, they will be ignored.

Parameters:
- `sourceCode` (String): No description available

### extractMethods

---
## Class: JavaCodeParser
### Method: extractMethods
#### Return Type: List<MethodInfo>
#### Parameters: classDeclaration: ClassOrInterfaceDeclaration

---

### Description:
The `extractMethods` method is a part of the `JavaCodeParser` class. Its primary purpose is to parse a given class or interface declaration and extract all the methods declared within it. This method is particularly useful when you need to analyze the structure of a Java class or interface programmatically.

### Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): This is the class or interface declaration from which the method information will be extracted. The declaration should be an instance of ClassOrInterfaceDeclaration, a class provided by the JavaParser library.

### Return Value:
This method returns a List of `MethodInfo` objects. Each `MethodInfo` object contains information about a method declared in the provided class or interface declaration. The returned list includes all methods in the class or interface, including public, protected, private, and package-private methods.

### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDeclaration = ... // obtain class declaration
List<MethodInfo> methods = parser.extractMethods(classDeclaration);

for (MethodInfo method : methods) {
    System.out.println("Method Name: " + method.getName());
    System.out.println("Return Type: " + method.getReturnType());
    // other operations...
}
```

### Important Notes:
- The `extractMethods` method does not extract methods from inner classes or interfaces. If you need to extract methods from inner classes or interfaces, you will need to retrieve their declarations separately and call `extractMethods` for each one.
- The method does not differentiate between class and interface methods. It treats them the same way and extracts the method information accordingly.
- The method does not extract constructor information. Only actual methods are considered.
- The method will return an empty list if the provided class or interface does not declare any methods.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractParameters

---
## Class: JavaCodeParser
### Method: extractParameters

---
#### Description:
The `extractParameters` method of the `JavaCodeParser` class is designed to extract and return a list of parameters from a given method declaration in a Java code.

#### Parameters:
- `method` (MethodDeclaration): The method declaration from which the parameters are to be extracted. This should be an instance of the `MethodDeclaration` class.

#### Return Value:
This method returns a `List<ParameterInfo>`. Each `ParameterInfo` object in the list represents a parameter in the given method declaration, including its type and name.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
MethodDeclaration methodDeclaration = getMethodDeclaration(); // Assume this retrieves a MethodDeclaration instance
List<ParameterInfo> parameters = parser.extractParameters(methodDeclaration);

for (ParameterInfo parameter : parameters) {
    System.out.println("Parameter Name: " + parameter.getName());
    System.out.println("Parameter Type: " + parameter.getType());
}
```
In the above example, we first create an instance of `JavaCodeParser`. Then, we retrieve a `MethodDeclaration` instance (the retrieval process is not shown). We then call `extractParameters` on our parser instance, passing in the `methodDeclaration`. The method returns a list of `ParameterInfo` objects, which we iterate over to print out the name and type of each parameter.

#### Important Notes:
- The `extractParameters` method does not modify the given `MethodDeclaration` object. It only reads and extracts information from it.
- If the given `MethodDeclaration` object does not have any parameters, the method will return an empty list.
- The order of the `ParameterInfo` objects in the returned list matches the order of the parameters in the original method declaration.
- The method does not handle any exceptions. If the provided `MethodDeclaration` object is null, the method will throw a `NullPointerException`.

Parameters:
- `method` (MethodDeclaration): No description available

### extractFields

---
## Class: JavaCodeParser
### Method: extractFields
---
#### Description:
The `extractFields` method is designed to parse a given Java class or interface declaration and extract all field information present. This includes field names, types, access modifiers, and annotations.

#### Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): This parameter represents the Java class or interface declaration that needs to be parsed. It should be an instance of the `ClassOrInterfaceDeclaration` class.

#### Returns:
This method returns a `List<FieldInfo>`. Each `FieldInfo` object in the list represents a field in the provided class or interface declaration. The `FieldInfo` object includes details such as the field name, type, access modifiers, and any annotations present.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDeclaration = ... // obtain class declaration
List<FieldInfo> fields = parser.extractFields(classDeclaration);

for (FieldInfo field : fields) {
    System.out.println("Field Name: " + field.getName());
    System.out.println("Field Type: " + field.getType());
    System.out.println("Access Modifiers: " + field.getModifiers());
    System.out.println("Annotations: " + field.getAnnotations());
}
```
In this example, we first create an instance of `JavaCodeParser`. Then, we obtain a `ClassOrInterfaceDeclaration` object representing the class or interface we want to parse. We pass this object to the `extractFields` method, which returns a list of `FieldInfo` objects. We then iterate over this list, printing out the name, type, access modifiers, and annotations of each field.

#### Important Notes:
- The `extractFields` method only extracts fields from the top level of the provided class or interface declaration. It does not extract fields from nested or inner classes or interfaces.
- If the provided `ClassOrInterfaceDeclaration` object does not contain any fields, the method will return an empty list.
- The method does not handle parsing errors. If the provided `ClassOrInterfaceDeclaration` is not a valid Java class or interface declaration, the behavior of the method is undefined.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractSuperClass

---
## Method Documentation

### Class: JavaCodeParser
### Method: extractSuperClass

#### Description:
The `extractSuperClass` method is designed to parse a given Java class or interface declaration and extract the name of its superclass, if any.

#### Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): This parameter represents the Java class or interface declaration that is to be parsed. It should be an instance of the `ClassOrInterfaceDeclaration` class.

#### Return Value:
This method returns a `String?` value. The returned string represents the name of the superclass of the provided class or interface declaration. If the provided class or interface does not extend any superclass, the method will return `null`.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDecl = ... // obtain class declaration
String superClass = parser.extractSuperClass(classDecl);
if (superClass != null) {
    System.out.println("Superclass name: " + superClass);
} else {
    System.out.println("No superclass found.");
}
```

#### Important Notes:
- This method only extracts the direct superclass of the provided class or interface declaration. If the superclass itself extends another class, this method will not return the name of that class.
- This method does not handle parsing errors. If the provided `ClassOrInterfaceDeclaration` instance is not a valid Java class or interface declaration, the behavior of this method is undefined.
- This method does not consider Java's default superclass `java.lang.Object`. If the provided class does not explicitly extend a superclass, this method will return `null`, not `java.lang.Object`.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractInterfaces

## JavaCodeParser.extractInterfaces

### Method Description:
The `extractInterfaces` method of the `JavaCodeParser` class is designed to parse a given class or interface declaration and extract the names of all interfaces that the class or interface directly implements or extends. 

### Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): This is the class or interface declaration that will be parsed. It represents the source code of a Java class or interface.

### Return Value:
This method returns a `List<String>`, which contains the names of all interfaces that the input class or interface directly implements or extends. If the input class or interface does not implement or extend any interfaces, the method will return an empty list.

### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDeclaration = ... // obtain class declaration
List<String> interfaces = parser.extractInterfaces(classDeclaration);
for (String interfaceName : interfaces) {
    System.out.println(interfaceName);
}
```
In this example, we create an instance of `JavaCodeParser`, obtain a `ClassOrInterfaceDeclaration` (not shown), and then use the `extractInterfaces` method to get a list of interface names. We then print each interface name to the console.

### Important Notes:
- This method only extracts interfaces that the class or interface directly implements or extends. It does not include interfaces that are implemented or extended by parent classes or interfaces.
- The order of the interface names in the returned list matches the order in which they appear in the class or interface declaration.
- If the `classDeclaration` parameter is null, the method will throw a `NullPointerException`.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractTypeParameters

---
## Class: JavaCodeParser
### Method: extractTypeParameters

#### Description:
The `extractTypeParameters` method is designed to parse through a given class or interface declaration and extract all type parameters present within it. This method is particularly useful when you need to analyze or manipulate the type parameters of a class or interface in a Java codebase.

#### Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): This is the class or interface declaration from which the type parameters are to be extracted. The declaration should be an instance of `ClassOrInterfaceDeclaration`, which is part of the JavaParser library.

#### Return Value:
This method returns a `List<String>`, which contains the names of all type parameters found in the provided class or interface declaration. If no type parameters are found, the method will return an empty list.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration declaration = ... // obtain ClassOrInterfaceDeclaration instance
List<String> typeParameters = parser.extractTypeParameters(declaration);
for (String typeParam : typeParameters) {
    System.out.println("Type Parameter: " + typeParam);
}
```
In this example, we first create an instance of `JavaCodeParser`. We then obtain a `ClassOrInterfaceDeclaration` instance (not shown), which represents the class or interface we're interested in. We then call `extractTypeParameters` on this declaration, and print out each type parameter that was found.

#### Important Notes:
- The `ClassOrInterfaceDeclaration` instance should be correctly initialized and represent a valid class or interface. If the instance is null or does not represent a valid class or interface, the behavior of the method is undefined.
- The returned list is a new list containing the type parameters. Any modifications to this list will not affect the original class or interface declaration.
- This method does not recursively extract type parameters from inner classes or interfaces. It only extracts type parameters from the top-level class or interface represented by the provided `ClassOrInterfaceDeclaration` instance.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractAnnotations

---
## Class: JavaCodeParser
### Method: extractAnnotations

#### Description:
The `extractAnnotations` method is a part of the `JavaCodeParser` class. Its primary purpose is to parse and extract all annotations from a given node in a Java code abstract syntax tree (AST). 

#### Parameters:
- `node: NodeWithAnnotations<*>` - This is an instance of the `NodeWithAnnotations` class from the JavaParser library. The node represents a part of the Java code AST that can potentially have annotations.

#### Return Value:
This method returns a `List<AnnotationInfo>`. Each `AnnotationInfo` object in the list represents an annotation extracted from the input node. The `AnnotationInfo` object includes details about the annotation such as its name, parameters, and associated values.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
NodeWithAnnotations<?> node = ... // obtain a node from a JavaParser AST
List<AnnotationInfo> annotations = parser.extractAnnotations(node);
for (AnnotationInfo annotation : annotations) {
    System.out.println("Found annotation: " + annotation.getName());
}
```
In this example, a `JavaCodeParser` instance is created and used to extract annotations from a `NodeWithAnnotations` instance. The extracted annotations are then printed to the console.

#### Important Notes:
- If the input node does not contain any annotations, the method will return an empty list.
- The method does not recursively search for annotations in the child nodes of the input node. It only extracts annotations directly associated with the input node.
- The method might throw a `ParseException` if the input node contains invalid or unrecognizable annotation syntax. It is recommended to handle this exception in the calling code.

Parameters:
- `node` (NodeWithAnnotations<*>): No description available

### extractAnnotationAttributes

---
## Class: JavaCodeParser
### Method: extractAnnotationAttributes
---
#### Description:
The `extractAnnotationAttributes` method is designed to parse and extract attributes from a given Java annotation expression. It provides a way to retrieve all the key-value pairs defined in an annotation.

#### Parameters:
- `annotation: AnnotationExpr` - This is the annotation expression from which the attributes will be extracted. `AnnotationExpr` is an object that represents an annotation in a Java source code.

#### Return Value:
This method returns a `Map<String, String>`. The map's keys are the names of the attributes in the annotation, and the values are the corresponding attribute values. If the annotation does not have any attributes, an empty map will be returned.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
AnnotationExpr annotation = new NormalAnnotationExpr(
    new Name("MyAnnotation"),
    NodeList.nodeList(
        new MemberValuePair("key1", new StringLiteralExpr("value1")),
        new MemberValuePair("key2", new StringLiteralExpr("value2"))
    )
);
Map<String, String> attributes = parser.extractAnnotationAttributes(annotation);
```
In this example, the `extractAnnotationAttributes` method is used to extract the attributes from a `MyAnnotation` annotation. The returned map will contain two entries: `{"key1"="value1", "key2"="value2"}`.

#### Important Notes:
- The method does not check if the provided annotation expression is valid. If an invalid annotation is provided, the behavior is undefined.
- The method does not handle nested annotations. If an attribute's value is another annotation, the value in the returned map will be the string representation of the nested annotation.
- The method treats all attribute values as strings. If an attribute's value is a non-string literal (e.g., an integer, a boolean), the value in the returned map will be the string representation of the literal.

Parameters:
- `annotation` (AnnotationExpr): No description available

### extractDocumentation

---
## Class: JavaCodeParser
### Method: extractDocumentation

#### Description:
The `extractDocumentation` method is designed to parse and extract the documentation information from a given Java code node. It is a part of the `JavaCodeParser` class and is used to retrieve the Javadoc comments associated with a specific node in the Java code.

#### Parameters:
- `node: NodeWithJavadoc<*>`: This parameter represents the Java code node from which the documentation information is to be extracted. The node should be of the type `NodeWithJavadoc`.

#### Return Value:
The method returns an instance of `DocumentationInfo?`. This object encapsulates the documentation information extracted from the specified node. If no documentation is found for the provided node, the method will return `null`.

#### Usage Example:
```java
NodeWithJavadoc<?> node = ...; // Obtain a node from Java code
JavaCodeParser parser = new JavaCodeParser();
DocumentationInfo? docInfo = parser.extractDocumentation(node);
if (docInfo != null) {
    System.out.println("Documentation: " + docInfo.getDocumentation());
} else {
    System.out.println("No documentation found for this node.");
}
```

#### Important Notes:
- The `extractDocumentation` method only retrieves the documentation associated with the provided node. It does not parse or interpret the content of the documentation.
- If the provided node does not have any associated Javadoc comments, the method will return `null`.
- The method does not handle any parsing errors. If the provided node is not valid or if there are any issues during the parsing process, an exception may be thrown.

Parameters:
- `node` (NodeWithJavadoc<*>): No description available

### extractJavadocParams

---
## Class: JavaCodeParser
### Method: extractJavadocParams

**Description:**
The `extractJavadocParams` method of the `JavaCodeParser` class is designed to parse a given Javadoc comment and extract the parameters and their descriptions. This method is particularly useful when you need to programmatically analyze or manipulate Javadoc comments in your Java code.

**Parameters:**
- `javadoc: Javadoc` - This is the Javadoc comment that needs to be parsed. The Javadoc comment should be passed as an instance of the `Javadoc` class.

**Return Value:**
This method returns a `Map<String, String>` where the key is the parameter name and the value is the parameter description. If the provided Javadoc comment does not contain any parameters, the method will return an empty map.

**Usage Example:**
```java
JavaCodeParser parser = new JavaCodeParser();
Javadoc javadoc = new Javadoc("/** @param name the name of the user. @param age the age of the user. */");
Map<String, String> params = parser.extractJavadocParams(javadoc);
for (Map.Entry<String, String> entry : params.entrySet()) {
    System.out.println("Parameter: " + entry.getKey() + ", Description: " + entry.getValue());
}
```
In this example, the `extractJavadocParams` method will parse the Javadoc comment and extract two parameters: `name` with the description "the name of the user" and `age` with the description "the age of the user".

**Important Notes:**
- The method does not validate the provided Javadoc comment. If the comment is not a valid Javadoc comment, the behavior of the method is undefined.
- The method assumes that the parameter descriptions in the Javadoc comment are written in English. If the descriptions are written in a different language, the method may not be able to correctly extract the parameters and their descriptions.
- The method does not handle Javadoc tags other than `@param`. If the comment contains other tags, they will be ignored.
- The method is case-sensitive. It treats parameter names as case-sensitive strings, so "paramName" and "paramname" would be considered as two different parameters.
- The method does not handle duplicate parameter names. If the Javadoc comment contains multiple `@param` tags with the same parameter name, the method will only keep the last one.

Parameters:
- `javadoc` (Javadoc): No description available

### extractJavadocReturns

---
# Class: JavaCodeParser
## Method: extractJavadocReturns

### Description:
The `extractJavadocReturns` method is designed to parse a provided Javadoc comment and extract the `@return` tag description. This method is particularly useful when you need to programmatically analyze the return value documentation of a Java method.

### Parameters:
- `javadoc` (Javadoc): The Javadoc comment to parse. This should be a valid Javadoc comment block associated with a Java method.

### Returns:
- `String?`: The method returns a nullable String. If the provided Javadoc comment includes a `@return` tag, the method will return the description associated with this tag. If the `@return` tag is not present or the description is empty, the method will return null.

### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
Javadoc javadoc = new Javadoc("/** @return the sum of the two numbers */");
String returnDescription = parser.extractJavadocReturns(javadoc);
System.out.println(returnDescription);  // Outputs: "the sum of the two numbers"
```

### Notes:
- The method expects a valid Javadoc comment. Providing an invalid Javadoc comment may lead to unexpected results or exceptions.
- The method only extracts the description of the `@return` tag. Other tags present in the Javadoc comment are ignored.
- The method does not validate the content of the `@return` tag description. If the description is present but empty, the method will return an empty string.
- The method is case-sensitive and expects the `@return` tag to be in lower case.

Parameters:
- `javadoc` (Javadoc): No description available

### extractJavadocThrows

---
### Method Documentation

#### Class: JavaCodeParser
#### Method: extractJavadocThrows
#### Return Type: Map<String, String>

**Description:**

The `extractJavadocThrows` method is designed to parse the provided Javadoc and extract all `@throws` tags, along with their corresponding descriptions. This method is particularly useful when you need to understand the exceptions that a method might throw, as documented in its Javadoc.

**Parameters:**

- `javadoc` (Javadoc): The Javadoc object that needs to be parsed. This object should contain the Javadoc comments from which the `@throws` tags and their descriptions will be extracted.

**Return Value:**

This method returns a `Map<String, String>` where:

- The key (String) is the exception name as mentioned in the `@throws` tag.
- The value (String) is the description of the exception as provided in the Javadoc.

**Usage Example:**

```java
JavaCodeParser parser = new JavaCodeParser();
Javadoc javadoc = new Javadoc("/** @throws IllegalArgumentException if the argument is null. */");
Map<String, String> throwsTags = parser.extractJavadocThrows(javadoc);
```

In this example, `throwsTags` will be a map containing one entry: `{IllegalArgumentException=if the argument is null}`.

**Notes:**

- If the provided Javadoc does not contain any `@throws` tags, the method will return an empty map.
- If the `@throws` tag does not have a description, the corresponding map value will be an empty string.
- The method does not validate the correctness of the exception names or their descriptions. It simply extracts them as they are from the Javadoc.
- The method is case-sensitive. It will not recognize tags like `@Throws` or `@THROWS`. Only `@throws` is considered valid.

Parameters:
- `javadoc` (Javadoc): No description available

### extractJavadocSeeAlso

---
## Class: JavaCodeParser

### Method: extractJavadocSeeAlso

#### Description:

The `extractJavadocSeeAlso` method is designed to parse Javadoc comments from a given Javadoc object and extract all `@see` or `@link` references. These references are typically used in Javadoc comments to point to other relevant pieces of code.

#### Parameters:

The method takes one parameter:

- `javadoc` (Javadoc): The Javadoc object from which `@see` or `@link` references are to be extracted. This object should represent the Javadoc comments associated with a specific piece of code.

#### Return Value:

This method returns a List of Strings (`List<String>`). Each String in the list represents a `@see` or `@link` reference found in the provided Javadoc object. If no such references are found, the method will return an empty list.

#### Usage Example:

```java
Javadoc javadoc = new Javadoc("/**\n * @see #method()\n * @link package.Class#method()\n */");
JavaCodeParser parser = new JavaCodeParser();
List<String> references = parser.extractJavadocSeeAlso(javadoc);

for(String reference : references) {
    System.out.println(reference);
}
```

In this example, the `extractJavadocSeeAlso` method will return a list containing two references: `#method()` and `package.Class#method()`.

#### Notes:

- The method only extracts `@see` and `@link` references. Other types of references or annotations will be ignored.
- The method does not validate the extracted references. It is the responsibility of the user to ensure that the references are valid and point to existing pieces of code.
- The method does not handle Javadoc parsing errors. If the provided Javadoc object is not properly formatted, the method may not work as expected.

Parameters:
- `javadoc` (Javadoc): No description available

### extractJavadocSince

---
## Class: JavaCodeParser
### Method: extractJavadocSince
---
**Description:**

The `extractJavadocSince` method in the `JavaCodeParser` class is used to extract the "Since" tag information from a given Javadoc comment.

**Parameters:**

The method accepts a single parameter:

- `javadoc` (Javadoc): This is the Javadoc comment from which the "Since" tag information is to be extracted.

**Return Value:**

The method returns a `String` value. The returned string contains the "Since" tag information extracted from the provided Javadoc comment. If the "Since" tag is not present in the provided Javadoc comment, the method returns `null`.

**Usage Example:**

Here is an example of how to use the `extractJavadocSince` method:

```java
JavaCodeParser parser = new JavaCodeParser();
Javadoc javadoc = new Javadoc("/**\n * This is a test method.\n * @since 1.0\n */");
String sinceTag = parser.extractJavadocSince(javadoc);
System.out.println(sinceTag);  // Outputs: "1.0"
```

In this example, the "Since" tag information ("1.0") is extracted from the provided Javadoc comment.

**Important Notes:**

- The method only extracts the "Since" tag information. It does not modify the provided Javadoc comment in any way.
- If the provided Javadoc comment does not contain a "Since" tag, the method returns `null`.
- The method does not handle any exceptions. If the provided Javadoc comment is `null`, a `NullPointerException` will be thrown.

Parameters:
- `javadoc` (Javadoc): No description available

### extractJavadocDeprecated

---
## Class: JavaCodeParser
### Method: extractJavadocDeprecated

**Description:** 

The `extractJavadocDeprecated` method is designed to parse the provided Javadoc and extract any `@deprecated` tags present within it. This method is useful when you need to identify deprecated methods or classes in a Java codebase.

**Parameters:**

- `javadoc: Javadoc` - The Javadoc object that the method will parse. This object should represent the Javadoc comments of a Java class or method.

**Return Value:**

- `String?` - The method returns a nullable String. If a `@deprecated` tag is found in the provided Javadoc, the method will return the text following the `@deprecated` tag. If no `@deprecated` tag is found, the method will return `null`.

**Usage Example:**

```java
Javadoc javadoc = new Javadoc("/**\n * @deprecated Use the newMethod() instead.\n */");
JavaCodeParser parser = new JavaCodeParser();
String deprecatedText = parser.extractJavadocDeprecated(javadoc);
System.out.println(deprecatedText);
// Output: Use the newMethod() instead.
```

**Important Notes:**

- The method only returns the first `@deprecated` tag it encounters. If there are multiple `@deprecated` tags in the Javadoc, subsequent ones will be ignored.
- The method does not include the `@deprecated` tag in the returned String, only the text that follows it.
- The method does not handle any formatting present in the Javadoc. If the text following the `@deprecated` tag includes any Javadoc formatting (like HTML tags), these will be included in the returned String.
- The method does not check if the provided Javadoc is valid or well-formed. If the Javadoc is malformed, the method's behavior is undefined.

Parameters:
- `javadoc` (Javadoc): No description available

### extractDependencies

## Method Documentation

### Class: JavaCodeParser
### Method: extractDependencies

#### Description:
The `extractDependencies` method is designed to parse through a given Java source code file (represented as a `CompilationUnit`) and extract a list of all dependencies (imported classes or interfaces) that are used within a specific class or interface declaration.

#### Parameters:
- `compilationUnit` (CompilationUnit): This is an object representation of the entire Java source code file that is to be parsed. It should be an instance of the `CompilationUnit` class, which is part of the JavaParser library.
- `classDeclaration` (ClassOrInterfaceDeclaration): This is an object representation of the specific class or interface within the `compilationUnit` from which dependencies are to be extracted. It should be an instance of the `ClassOrInterfaceDeclaration` class, which is also part of the JavaParser library.

#### Return Value:
This method returns a `List<String>`, where each string in the list is the fully qualified name of a class or interface that the specified `classDeclaration` is dependent on (i.e., classes or interfaces that are imported and used within the class or interface).

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
CompilationUnit cu = StaticJavaParser.parse(new File("MyJavaFile.java"));
ClassOrInterfaceDeclaration classDec = cu.getClassByName("MyClass").get();

List<String> dependencies = parser.extractDependencies(cu, classDec);
for (String dependency : dependencies) {
    System.out.println(dependency);
}
```
In this example, the `extractDependencies` method is used to parse a Java file named "MyJavaFile.java", extract the dependencies of a class named "MyClass", and print each dependency to the console.

#### Important Notes:
- The `extractDependencies` method only considers direct dependencies of the specified class or interface. It does not consider transitive dependencies (i.e., dependencies of dependencies).
- The method does not distinguish between different types of dependencies (e.g., classes vs. interfaces, or system-provided vs. user-defined). All dependencies are treated equally.
- If the specified `classDeclaration` does not exist within the `compilationUnit`, or if it has no dependencies, the method will return an empty list.

Parameters:
- `compilationUnit` (CompilationUnit): No description available
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractMethodDependencies

---
## Class: JavaCodeParser
### Method: extractMethodDependencies

#### Description
The `extractMethodDependencies` method is designed to parse a given Java class or interface declaration and extract all the dependencies that the class or interface has. It identifies all the classes or interfaces that the provided declaration depends on, which includes the classes or interfaces that are extended, implemented, or used within the class or interface.

#### Parameters
- `classDeclaration` (ClassOrInterfaceDeclaration): This is the Java class or interface declaration that you want to analyze. It should be an instance of `ClassOrInterfaceDeclaration`, which is a part of the JavaParser library.

#### Return Value
This method returns a `List<String>`. Each string in the list represents the fully qualified name of a class or interface that the provided declaration depends on. If the provided declaration has no dependencies, the method will return an empty list.

#### Usage Example
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDecl = ... // Obtain a ClassOrInterfaceDeclaration instance
List<String> dependencies = parser.extractMethodDependencies(classDecl);
for (String dependency : dependencies) {
    System.out.println(dependency);
}
```
In this example, we first create an instance of `JavaCodeParser`. We then obtain a `ClassOrInterfaceDeclaration` instance, which represents the class or interface we want to analyze. We call `extractMethodDependencies` with this instance to get a list of dependencies, and then print each dependency.

#### Important Notes
- This method does not consider dependencies that are only present in comments or string literals.
- The method does not handle circular dependencies. If a class or interface depends on itself, either directly or indirectly, the method may enter an infinite loop.
- The method assumes that the provided `ClassOrInterfaceDeclaration` instance is valid. If it is null or otherwise invalid, the method may throw an exception.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractFieldDependencies

---
Class: JavaCodeParser
Method: extractFieldDependencies
Return Type: List<String>
Parameters: classDeclaration: ClassOrInterfaceDeclaration

**Method Documentation:**

**Description:**

The `extractFieldDependencies` method of the `JavaCodeParser` class is designed to analyze a given Java class or interface declaration and extract all field dependencies. This method is particularly useful for understanding the relationships and dependencies between different parts of a Java codebase.

**Parameters:**

- `classDeclaration` (ClassOrInterfaceDeclaration): This is the input parameter representing the Java class or interface declaration to be analyzed. The method will parse this declaration to identify and extract all field dependencies.

**Return Value:**

This method returns a `List<String>`, where each String in the list represents a field dependency identified in the provided class or interface declaration. If no dependencies are found, the method will return an empty list.

**Usage Example:**

```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration classDeclaration = ...; // Obtain class declaration
List<String> dependencies = parser.extractFieldDependencies(classDeclaration);
for (String dependency : dependencies) {
    System.out.println(dependency);
}
```

**Important Notes:**

- The method only identifies field dependencies within the provided class or interface declaration. Dependencies in nested or inner classes/interfaces will not be identified.
- The method does not identify dependencies in method bodies.
- The method does not identify dependencies on primitive types or on types from the java.lang package.
- The method assumes that the provided `ClassOrInterfaceDeclaration` is a valid and well-formed Java class or interface declaration. Providing an invalid declaration may result in undefined behavior or errors.
- The returned list of dependencies does not preserve any particular order.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractAnnotationDependencies

---
## Class: JavaCodeParser
### Method: extractAnnotationDependencies

#### Description:
The `extractAnnotationDependencies` method is designed to parse a given Java class or interface declaration and extract all annotation dependencies. This method is particularly useful when you need to understand the various annotations that a class or interface is dependent on, which can be crucial for understanding the overall structure and behavior of a Java program.

#### Parameters:
- **classDeclaration (ClassOrInterfaceDeclaration):** This is the class or interface declaration that you want to parse. The declaration should be provided as an instance of the `ClassOrInterfaceDeclaration` class.

#### Return Value:
This method returns a `List<String>`. Each string in the list represents a unique annotation dependency found in the provided class or interface declaration. If no annotation dependencies are found, the method will return an empty list.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
ClassOrInterfaceDeclaration declaration = ... // obtain class or interface declaration
List<String> annotationDependencies = parser.extractAnnotationDependencies(declaration);

for (String dependency : annotationDependencies) {
    System.out.println(dependency);
}
```
In this example, we first create an instance of `JavaCodeParser`. Then, we obtain a `ClassOrInterfaceDeclaration` that we want to parse. We call the `extractAnnotationDependencies` method on the parser, passing in the declaration. The method returns a list of annotation dependencies, which we then print out.

#### Important Notes:
- The method only extracts direct annotation dependencies from the provided class or interface declaration. It does not recursively check for annotations on those dependencies.
- The method does not distinguish between standard Java annotations and custom annotations. Any annotation found in the class or interface declaration is considered a dependency.
- The method does not handle parsing errors. If the provided `ClassOrInterfaceDeclaration` is not valid, the method may throw an exception.

Parameters:
- `classDeclaration` (ClassOrInterfaceDeclaration): No description available

### extractMethodInvocations

---
## Class: JavaCodeParser
### Method: extractMethodInvocations

#### Description:
The `extractMethodInvocations` method is designed to parse a given Java method declaration and extract all method invocations within it. This method is particularly useful for code analysis, debugging, and refactoring tasks.

#### Parameters:
- `method` (MethodDeclaration): The Java method declaration to be parsed. This should be a valid Java method declaration.

#### Returns:
- `List<String>`: This method returns a list of strings, where each string represents a method invocation found within the provided method declaration. If no method invocations are found, an empty list is returned. 

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
MethodDeclaration method = AST.parseMethodDeclaration("public void test() { System.out.println(); }");
List<String> invocations = parser.extractMethodInvocations(method);
for (String invocation : invocations) {
    System.out.println(invocation);
}
```
In this example, the `extractMethodInvocations` method is used to parse a simple method declaration and print out all method invocations found within it. The output will be `System.out.println`.

#### Notes:
- The `extractMethodInvocations` method only extracts method invocations that are directly called within the provided method declaration. It does not extract method invocations that are called within nested class or method declarations.
- The returned list of method invocations does not include any method invocation arguments or return types. Each string in the list only includes the method name and any class or object it is invoked on.
- The `extractMethodInvocations` method does not handle any syntax errors in the provided method declaration. If the provided method declaration is not a valid Java method declaration, the behavior of this method is undefined.

Parameters:
- `method` (MethodDeclaration): No description available

### extractFieldAccesses

---
## Class: JavaCodeParser

### Method: extractFieldAccesses

#### Description:
The `extractFieldAccesses` method is designed to parse a given Java method declaration and extract all field accesses within that method. This method is particularly useful when you need to analyze the dependencies of a method on the class fields.

#### Parameters:
- `method` (MethodDeclaration): The Java method declaration to be parsed. This should be an instance of the `MethodDeclaration` class.

#### Return Value:
The method returns a `Set<String>`. Each string in the set represents a field that is accessed within the provided method declaration. The set does not contain any duplicate entries, even if a field is accessed multiple times within the method.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
MethodDeclaration methodDeclaration = AST.parseMethodDeclaration("public void setX(int x) { this.x = x; }");
Set<String> fieldAccesses = parser.extractFieldAccesses(methodDeclaration);
System.out.println(fieldAccesses);  // Outputs: ["x"]
```
In this example, the `extractFieldAccesses` method is used to parse a simple setter method and it correctly identifies that the field `x` is accessed.

#### Important Notes:
- The method only identifies direct field accesses. If a field is accessed through a method call (for example, a getter or setter), this method will not identify that field as being accessed.
- The method does not identify the type of access (read or write) to the fields. It only identifies that the field is accessed.
- The method does not handle nested classes. If the provided method declaration is from a nested class, the method may not correctly identify all field accesses.
- The method does not handle inheritance. If the provided method declaration is from a subclass and accesses fields from a superclass, the method may not correctly identify all field accesses.

Parameters:
- `method` (MethodDeclaration): No description available

### extractLocalVariables

---
## Class: JavaCodeParser

### Method: extractLocalVariables

#### Description:
The `extractLocalVariables` method of the `JavaCodeParser` class is designed to parse a given method declaration and extract the names of all local variables declared within that method.

#### Parameters:
- `method` (MethodDeclaration): This is the method declaration that will be parsed to extract the local variables. The `MethodDeclaration` should be an instance of the `MethodDeclaration` class, which represents a method declaration in the Java programming language.

#### Return Value:
This method returns a `Set<String>`, which contains the names of all local variables declared in the provided method declaration. The `Set` ensures that the returned collection of variable names is unique, i.e., it does not contain any duplicate entries.

#### Usage Example:
```java
JavaCodeParser parser = new JavaCodeParser();
MethodDeclaration methodDeclaration = getMethodDeclaration(); // Assume this returns a valid MethodDeclaration
Set<String> localVariables = parser.extractLocalVariables(methodDeclaration);

for(String variable : localVariables) {
    System.out.println(variable);
}
```
In the above example, `getMethodDeclaration()` is a hypothetical method that returns a `MethodDeclaration` object. The `extractLocalVariables` method is then used to extract the local variables from this method declaration.

#### Important Notes:
- The `extractLocalVariables` method only extracts local variables from the provided method declaration. It does not extract variables declared in nested classes, interfaces, or enums within the method.
- The method does not extract parameters of the provided method declaration as local variables.
- The method does not extract variables from inner methods within the provided method declaration.
- If the provided method declaration does not contain any local variables, the method will return an empty set.
- The method does not consider the scope of the local variables. It will extract all local variables regardless of their scope (i.e., whether they are declared within a block, loop, or conditional statement).
- The method does not handle exceptions. If an invalid `MethodDeclaration` is provided, it may throw a runtime exception.

Parameters:
- `method` (MethodDeclaration): No description available

## Properties

### parser
- Type: JavaParser
- Modifiers: private, val

---
Class: JavaCodeParser
Property: parser
Type: JavaParser
Modifiers: private, val

## Property Documentation

### Description
The `parser` property in the `JavaCodeParser` class is a private, read-only instance of the `JavaParser` type. This property is responsible for parsing Java code into an Abstract Syntax Tree (AST). It is a crucial component of the `JavaCodeParser` class, enabling the analysis and manipulation of Java source code.

### Usage
The `parser` property is used internally within the `JavaCodeParser` class. It is primarily utilized in methods that require parsing of Java source code, such as code analysis, code transformation, or code generation methods. As a private property, it cannot be accessed directly from outside the `JavaCodeParser` class and is only used within the class itself.

### Side Effects and Implications
The `parser` property does not have any side effects as it is a read-only property. However, it is important to note that the parsing process can throw exceptions if the input source code is not a valid Java code. These exceptions should be handled appropriately within the methods that use the `parser` property.

### Usage Considerations
While using the `parser` property, it is important to ensure that the input source code is valid Java code to prevent exceptions during the parsing process. Additionally, as the `parser` property is read-only, it is initialized at the time of `JavaCodeParser` object creation and cannot be changed afterwards. Therefore, any configuration or settings for the `JavaParser` should be done at the time of `JavaCodeParser` object creation.
