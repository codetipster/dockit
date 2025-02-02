# KotlinCodeParser

# Class Reference: KotlinCodeParser

## Package: com.dockit.core.parser

The `KotlinCodeParser` class is a key component in the Dockit codebase, primarily responsible for parsing Kotlin source code files. It is part of the `com.dockit.core.parser` package.

## Class Description

The `KotlinCodeParser` class is designed to interpret and analyze Kotlin source code. Its primary role is to read Kotlin files, break down the code into understandable segments, and provide structured data that can be further processed or analyzed by other components in the system.

This class plays a crucial role in the codebase, acting as a bridge between raw Kotlin code and the system's internal representation of that code. By transforming the source code into a more manageable format, it enables other components to perform their tasks more effectively.

## Design Patterns and Principles

The `KotlinCodeParser` class follows the Interpreter design pattern. This pattern is used to define the grammar for instructions that are part of a language or an interface. In this case, the class interprets the Kotlin programming language. 

The class is also designed with the Single Responsibility Principle (SRP) in mind. Its sole responsibility is to parse Kotlin code, and it is not concerned with other tasks such as file I/O operations or data persistence. This makes the class more robust, easier to maintain, and less prone to bugs.

## Key Relationships

The `KotlinCodeParser` class interacts primarily with the `KotlinFileReader` and `CodeAnalyzer` classes. 

- `KotlinFileReader`: This class is responsible for reading Kotlin source files from the disk and providing the raw code to the `KotlinCodeParser` for parsing.

- `CodeAnalyzer`: Once the `KotlinCodeParser` has parsed the Kotlin code, the `CodeAnalyzer` class takes the structured data and performs various analysis tasks on it.

By understanding the role and responsibilities of the `KotlinCodeParser` class, developers can better utilize it when working with Kotlin source code within the Dockit system.

## Methods

### parse

---
## Class: KotlinCodeParser
### Method: parse

#### Description:
The `parse` method is a part of the `KotlinCodeParser` class. Its primary purpose is to analyze and parse the provided Kotlin source code, and extract information about the classes defined within it.

#### Parameters:
- `sourceCode` (String): This parameter represents the Kotlin source code to be parsed. It should be a valid Kotlin code as a string.

#### Return Value:
- `ClassInfo?`: This method returns an instance of the `ClassInfo` class, which contains information about the classes defined within the provided source code. If no class is found in the source code, the method returns null.

#### Usage Example:
```kotlin
val parser = KotlinCodeParser()
val sourceCode = """
    class Sample {
        var id: Int = 0
        var name: String = ""
    }
"""
val classInfo = parser.parse(sourceCode)
println(classInfo?.className) // Outputs: "Sample"
```

#### Important Notes:
- The `parse` method only analyzes the first class it encounters in the provided source code. If the source code contains multiple class definitions, information about subsequent classes will not be included in the returned `ClassInfo` instance.
- The `parse` method does not validate the provided source code. If the source code is not valid Kotlin code, the behavior of the method is undefined.
- The `parse` method does not handle inner or nested classes. If the first class in the source code is an inner or nested class, the method may not return accurate `ClassInfo`.
- The `parse` method is not thread-safe. If multiple threads call this method simultaneously with different source code, the results may be inconsistent.

Parameters:
- `sourceCode` (String): No description available

### extractMethods

---

**Class**: `KotlinCodeParser`

**Method**: `extractMethods`

**Return Type**: `List<MethodInfo>`

**Parameters**: `ktClass: KtClass`

---

**Method Documentation**

---

**Description**

The `extractMethods` method of the `KotlinCodeParser` class is designed to parse a given Kotlin class (`KtClass`) and extract all the methods present in it. This method is useful when you need to analyze the structure of a Kotlin class programmatically, for instance, for code analysis, refactoring, or documentation generation.

**Parameters**

- `ktClass: KtClass`: This is the Kotlin class from which the method information will be extracted. It represents the source code of a Kotlin class.

**Return Value**

This method returns a `List<MethodInfo>`. Each `MethodInfo` object in the list represents a method found in the provided Kotlin class. The `MethodInfo` object includes details about the method such as its name, return type, parameters, visibility, and whether it is abstract, final, or open.

**Usage Example**

```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktClass = KtClass("MyClass") // Assume MyClass is a valid Kotlin class
val methodsInfo = kotlinCodeParser.extractMethods(ktClass)

for (methodInfo in methodsInfo) {
    println("Method Name: ${methodInfo.name}")
    println("Return Type: ${methodInfo.returnType}")
    // Continue for other properties of MethodInfo
}
```

**Important Notes**

- The `extractMethods` method only extracts methods from the provided `KtClass`. It does not extract methods from any inner classes, anonymous classes, or lambda expressions present in the `KtClass`.
- The `extractMethods` method does not execute or evaluate any code present in the `KtClass`. It only analyzes the structure of the class.
- If the `KtClass` provided is not a valid Kotlin class, the behavior of the `extractMethods` method is undefined. It may throw an exception or return an empty list.

Parameters:
- `ktClass` (KtClass): No description available

### extractParameters

---
## Class: KotlinCodeParser

### Method: extractParameters

#### Description:
The `extractParameters` method is designed to parse a given Kotlin function and extract all the parameters used within it. This method is a part of the `KotlinCodeParser` class and is used to analyze and retrieve information about the parameters of a function in a Kotlin codebase.

#### Parameters:
- `function: KtNamedFunction`: This is the Kotlin function from which the parameters are to be extracted. The function must be an instance of `KtNamedFunction`.

#### Return Value:
This method returns a `List<ParameterInfo>`. Each `ParameterInfo` object in the list represents a parameter used in the provided Kotlin function. The `ParameterInfo` object contains details about the parameter such as its name, type, and default value (if any).

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val function: KtNamedFunction = getFunctionFromCode() // Assume this method returns a KtNamedFunction
val parameters: List<ParameterInfo> = kotlinCodeParser.extractParameters(function)

for (parameter in parameters) {
    println("Parameter Name: ${parameter.name}, Parameter Type: ${parameter.type}")
}
```
In the above example, `extractParameters` is used to get the parameters of a Kotlin function. The parameters are then printed to the console.

#### Notes:
- This method only extracts parameters from the function provided. It does not extract parameters from any nested or inner functions.
- If the provided function does not have any parameters, this method will return an empty list.
- The order of the `ParameterInfo` objects in the returned list matches the order of the parameters in the function declaration.
- This method does not handle parsing errors. If the provided function is not a valid Kotlin function, the behavior of this method is undefined.

Parameters:
- `function` (KtNamedFunction): No description available

### extractMethodModifiers

---
## Class: KotlinCodeParser

### Method: extractMethodModifiers

#### Description:
The `extractMethodModifiers` method is used to extract and return a list of modifiers from a given Kotlin function. Modifiers are keywords that you add to those definitions to change their meanings. In Kotlin, examples of function modifiers include `public`, `private`, `protected`, `internal`, `expect`, `external`, `final`, `open`, `abstract`, `sealed`, `const`, `lateinit`, `tailrec`, `vararg`, `noinline`, `crossinline`, `reified`, `suspend`, `actual`, etc.

#### Parameters:
- `function: KtNamedFunction`: This is a Kotlin function from which the method will extract the modifiers. `KtNamedFunction` is a class in the Kotlin PSI (Program Structure Interface) which represents a function declaration in the Kotlin language.

#### Return Value:
This method returns a `List<String>`. Each string in the list represents a modifier keyword extracted from the input function. If the function has no modifiers, the method will return an empty list.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val function: KtNamedFunction = ... // Assume this is a valid Kotlin function
val modifiers = kotlinCodeParser.extractMethodModifiers(function)
println("Modifiers: $modifiers")
```
In this example, we first create an instance of `KotlinCodeParser`. Then we call the `extractMethodModifiers` method on this instance, passing a `KtNamedFunction` instance as the parameter. The method will return a list of modifiers, which we then print.

#### Important Notes:
- The order of the modifiers in the returned list is not guaranteed to match the order in the original function declaration.
- The method does not throw any exceptions. If the input function has no modifiers, it will return an empty list.
- This method does not modify the input function in any way. It only reads and extracts information from it.
- The method does not handle annotations. If you need to extract annotations from the function, you should use a different method or class designed for that purpose.
- The method does not handle type parameters or return types. It only deals with modifiers.

Parameters:
- `function` (KtNamedFunction): No description available

### extractFields

---
## Class: KotlinCodeParser
### Method: extractFields(ktClass: KtClass): List<FieldInfo>

---
#### Description:
The `extractFields` method is designed to parse a Kotlin class (`KtClass`) and extract all the field information present within it. It provides a comprehensive overview of the fields, including their names, types, and other relevant details.

#### Parameters:
- `ktClass` (KtClass): This is the Kotlin class from which the field information will be extracted. The class should be an instance of `KtClass`.

#### Returns:
This method returns a `List<FieldInfo>`. Each `FieldInfo` object in the list represents a field in the provided Kotlin class. The `FieldInfo` object includes details such as the field's name, type, visibility, and whether it is mutable or not.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktClass = ... // Obtain an instance of KtClass
val fieldsInfo = kotlinCodeParser.extractFields(ktClass)

for (fieldInfo in fieldsInfo) {
    println("Field Name: ${fieldInfo.name}, Field Type: ${fieldInfo.type}")
}
```
In this example, an instance of `KotlinCodeParser` is created. An instance of `KtClass` is obtained and passed to the `extractFields` method. The method returns a list of `FieldInfo` objects, which is then iterated over to print the name and type of each field.

#### Important Notes:
- The `extractFields` method only extracts information about the fields that are directly declared in the provided `ktClass`. It does not extract information about fields that are inherited from superclasses.
- If the `ktClass` does not contain any fields, the method will return an empty list.
- The method does not modify the provided `ktClass` in any way. It only reads and extracts information from it.

Parameters:
- `ktClass` (KtClass): No description available

### hasBody

---
Class: KotlinCodeParser
Method: hasBody
Return Type: Boolean
Parameters: None

---

**Method Documentation:**

**Description:**

The `hasBody` method in the `KotlinCodeParser` class is used to determine whether a given Kotlin code block has a body or not. The method does not take any parameters.

**Parameters:**

This method does not require any parameters.

**Return Value:**

This method returns a `Boolean` value. If the Kotlin code block has a body, the method returns `true`. If the code block does not have a body, the method returns `false`.

**Usage Example:**

```kotlin
val parser = KotlinCodeParser()
val hasBody = parser.hasBody()
println("Does the code block have a body? $hasBody")
```

**Notes:**

This method assumes that the Kotlin code block is well-formed and syntactically correct. It does not perform any syntax checking or validation. If the code block is not valid, the behavior of this method is undefined. It is recommended to use this method after ensuring the code block's validity.

### extractDelegateType

---
## Class: KotlinCodeParser

### Method: extractDelegateType

#### Description:
The `extractDelegateType` method is used to extract the type of a delegate from a given Kotlin property delegate. This method is particularly useful when you need to determine the type of a delegate in order to perform further operations or analysis.

#### Parameters:
- `delegate` (KtPropertyDelegate): The Kotlin property delegate from which the delegate type is to be extracted. This is the input to the method.

#### Returns:
- `String`: This method returns a string representing the type of the delegate. If the delegate type cannot be determined, the method will return an empty string.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val delegateType = kotlinCodeParser.extractDelegateType(myDelegate)
println("Delegate Type: $delegateType")
```
In this example, `myDelegate` is a Kotlin property delegate. The `extractDelegateType` method is called to determine the type of `myDelegate`. The result is then printed to the console.

#### Notes:
- The `extractDelegateType` method does not modify the input delegate. It only analyses and returns the type of the delegate.
- If the delegate type cannot be determined, the method will return an empty string. It will not throw an exception. Therefore, it is recommended to check the return value before using it.
- The method does not support nested delegate types. If the delegate is of a nested type, the method will only return the outermost type.
- The method assumes that the input delegate is a valid Kotlin property delegate. If an invalid delegate is passed, the behavior of the method is undefined.

Parameters:
- `delegate` (KtPropertyDelegate): No description available

### extractPropertyModifiers

---
## Class: KotlinCodeParser
### Method: extractPropertyModifiers

**Description:**

The `extractPropertyModifiers` method of the `KotlinCodeParser` class is designed to parse a given `KtProperty` object and extract all the modifiers associated with the property. Modifiers are keywords that you add to those definitions to change their meanings. In Kotlin, these could be 'private', 'protected', 'public', etc.

**Parameters:**

- `property: KtProperty`: This is the Kotlin property from which the method will extract the modifiers. The `KtProperty` object should represent a valid Kotlin property.

**Return Value:**

This method returns a `List<String>`. Each string in the list represents a modifier keyword associated with the provided `KtProperty`. If the property has no modifiers, the method will return an empty list. The order of the modifiers in the list is not guaranteed to match the order in the original code.

**Usage Example:**

```kotlin
val kotlinCodeParser = KotlinCodeParser()
val property = KtPsiFactory(project).createProperty("private val exampleProperty: String")
val modifiers = kotlinCodeParser.extractPropertyModifiers(property)
println(modifiers) // Output: ["private"]
```

**Notes:**

- The `KtProperty` object passed as a parameter should be a valid Kotlin property. If an invalid `KtProperty` object is passed, the behavior of the method is undefined.
- The method does not check if the modifiers of the `KtProperty` are valid Kotlin modifiers. It simply extracts and returns them.
- The method does not modify the `KtProperty` object. It only reads and extracts information from it.
- The method is not thread-safe. If the `KtProperty` object is modified by another thread while this method is running, the result may be incorrect.

Parameters:
- `property` (KtProperty): No description available

### extractVisibility

## Class: KotlinCodeParser
### Method: extractVisibility

#### Description
The `extractVisibility` method is a part of the `KotlinCodeParser` class. Its primary purpose is to analyze a given Kotlin declaration and extract its visibility modifier. This method is particularly useful when you need to understand the visibility scope of a Kotlin declaration programmatically.

#### Parameters
The method accepts a single parameter:

- `element: KtDeclaration` - This is the Kotlin declaration from which the visibility modifier is to be extracted. `KtDeclaration` is a part of the Kotlin PSI (Program Structure Interface) and represents a declaration in the Kotlin language.

#### Return Value
The method returns a `String?` (nullable String). The returned string represents the visibility modifier of the provided Kotlin declaration. If the declaration does not have a visibility modifier, the method will return `null`.

#### Usage Example
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val declaration: KtDeclaration = getDeclaration() // Assume this is a method that returns a KtDeclaration
val visibility = kotlinCodeParser.extractVisibility(declaration)
println("The visibility of the declaration is: $visibility")
```
In this example, `extractVisibility` is used to get the visibility modifier of a Kotlin declaration. The visibility is then printed to the console.

#### Important Notes
- The `extractVisibility` method only extracts the visibility from the provided `KtDeclaration`. It does not modify the declaration in any way.
- If the provided `KtDeclaration` does not have a visibility modifier, the method will return `null`. It's important to handle this potential `null` value in your code to avoid `NullPointerException`s.
- The returned visibility modifier is a `String`, not an enumeration or a specific type. If you need to compare the result with a specific visibility keyword, ensure to use string comparison methods.

Parameters:
- `element` (KtDeclaration): No description available

### extractTypeParameters

---
## KotlinCodeParser.extractTypeParameters

### Description
The `extractTypeParameters` method of the `KotlinCodeParser` class is designed to parse a given Kotlin class and extract all type parameters used within the class. 

### Parameters
The method takes the following parameter:

- `ktClass: KtClass`: This is an instance of the `KtClass` class that represents the Kotlin class from which the type parameters are to be extracted. 

### Return Value
The method returns a `List<String>`. Each string in the list represents a type parameter used in the provided Kotlin class. If no type parameters are found in the class, an empty list is returned.

### Usage Example
Here is a practical example of how to use the `extractTypeParameters` method:

```kotlin
val ktClass = KtClass("class Example<T, R> {...}")
val parser = KotlinCodeParser()
val typeParameters = parser.extractTypeParameters(ktClass)

// typeParameters will contain ["T", "R"]
```

In this example, the `extractTypeParameters` method is used to extract the type parameters `T` and `R` from the `Example` class.

### Important Notes
- The `extractTypeParameters` method only extracts direct type parameters of the class. It does not extract type parameters from nested or inner classes.
- The method does not check for the validity of the type parameters. It is the responsibility of the caller to ensure that the provided `KtClass` instance represents a valid Kotlin class.
- The order of the type parameters in the returned list corresponds to the order in which they appear in the class definition.
- If the same type parameter is used multiple times in the class, it will only appear once in the returned list.

Parameters:
- `ktClass` (KtClass): No description available

### extractDocumentation

---

### Class: KotlinCodeParser

---

#### Method: extractDocumentation

---

**Description:**

The `extractDocumentation` method is a part of the `KotlinCodeParser` class. Its primary purpose is to parse a given Kotlin code element and extract the associated documentation, if any, from it.

---

**Parameters:**

- `element: KtElement` - This parameter represents the Kotlin code element from which the documentation needs to be extracted. The `KtElement` is a part of the Kotlin compiler's PSI (Program Structure Interface) and represents a piece of the Kotlin code.

---

**Return Value:**

The method returns a `DocumentationInfo?` object. This object encapsulates the extracted documentation from the provided Kotlin code element. If no documentation is associated with the given element, the method will return `null`.

---

**Usage Example:**

```kotlin
val parser = KotlinCodeParser()
val element: KtElement = // Obtain a KtElement
val documentationInfo: DocumentationInfo? = parser.extractDocumentation(element)

if (documentationInfo != null) {
    println("Extracted Documentation: ${documentationInfo.content}")
} else {
    println("No documentation found for this element.")
}
```

In this example, an instance of `KotlinCodeParser` is created and the `extractDocumentation` method is called with a `KtElement`. The returned `DocumentationInfo` object (if not `null`) is then used to print the extracted documentation.

---

**Important Notes:**

- The `extractDocumentation` method only extracts the documentation associated with the given `KtElement`. It does not parse or interpret the actual code within the element.
- The method can return `null` if no documentation is associated with the `KtElement`. It is recommended to always check for `null` before using the returned `DocumentationInfo` object.
- The method does not modify the given `KtElement` in any way. It only reads and extracts information from it.

Parameters:
- `element` (KtElement): No description available

### extractKotlinDocumentation

---
## Class: KotlinCodeParser
### Method: extractKotlinDocumentation

#### Description:
The `extractKotlinDocumentation` method is designed to parse a given Kotlin element and extract its documentation information. This method is typically used for generating documentation from Kotlin source code.

#### Parameters:
- `element: KtElement` - This is the Kotlin element from which the documentation is to be extracted. `KtElement` is a part of the Kotlin PSI (Program Structure Interface) and represents an element in the Kotlin language.

#### Return Value:
The method returns a `KotlinDocumentationInfo?` object. This object contains all the extracted documentation information from the provided Kotlin element. If no documentation is found or the extraction fails, the method returns `null`.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktElement = ... // Obtain a KtElement
val documentationInfo = kotlinCodeParser.extractKotlinDocumentation(ktElement)
if (documentationInfo != null) {
    println(documentationInfo)
}
```
In this example, we first create an instance of `KotlinCodeParser`. Then, we obtain a `KtElement` (the process for this is not shown as it depends on your specific use case). We then call `extractKotlinDocumentation` on the `KtElement` to extract the documentation information, which we print if it's not `null`.

#### Important Notes:
- The method does not modify the provided `KtElement`; it only reads from it.
- The method may return `null` if no documentation is found for the provided `KtElement`, or if an error occurs during the extraction process.
- The returned `KotlinDocumentationInfo` object (if not `null`) contains the extracted documentation as a string. It may need further parsing depending on your requirements.
- This method does not handle any exceptions. Any exceptions thrown during the extraction process need to be caught and handled by the caller.

Parameters:
- `element` (KtElement): No description available

### extractKDocDescription

---

## Class: KotlinCodeParser

### Method: extractKDocDescription

#### Description:
The `extractKDocDescription` method is designed to parse and extract the description from a given KDoc comment block in a Kotlin codebase. This method is particularly useful when you need to programmatically retrieve and manipulate the documentation comments in your Kotlin code.

#### Parameters:
- `kdoc: KDoc` - The KDoc object that represents a KDoc comment block in the Kotlin code. This object should be properly initialized and contain the KDoc comment block from which the description will be extracted.

#### Return Value:
This method returns a `String` that represents the extracted description from the provided KDoc comment block. If the KDoc does not contain a description, the method will return an empty string.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val kdoc = KDoc("/** This is a sample KDoc comment block. */")
val description = kotlinCodeParser.extractKDocDescription(kdoc)
println(description) // Outputs: "This is a sample KDoc comment block."
```

#### Important Notes:
- The KDoc object passed as a parameter should be properly initialized. If the KDoc object is null or does not contain a valid KDoc comment block, the method will throw an exception.
- The method only extracts the description part of the KDoc comment block. Any annotations, tags, or other special KDoc elements will not be included in the returned string.
- The method does not modify the original KDoc object. It only reads the KDoc comment block and extracts the description.
- The method does not handle any formatting or special characters in the KDoc comment block. The returned string will contain the description as it is in the original KDoc comment block.

Parameters:
- `kdoc` (KDoc): No description available

### extractKDocTags

## Method Documentation

### Class: KotlinCodeParser
### Method: extractKDocTags

#### Description:
The `extractKDocTags` method in the `KotlinCodeParser` class is designed to parse and extract specific tags from a given Kotlin Documentation Comment (KDoc). 

#### Parameters:
- `kdoc` (KDoc): The KDoc object from which the tags need to be extracted. This should be a valid KDoc object.
- `tagName` (String): The name of the tag to be extracted from the KDoc. This is a string that represents the tag name.

#### Returns:
- `Map<String, String>`: This method returns a map where the keys are the names of the tags extracted from the KDoc and the values are the corresponding tag descriptions. If the specified tag is not found in the KDoc, the method will return an empty map.

#### Usage Example:
```kotlin
val kdoc = KDoc("This is a sample KDoc with @param tag")
val kotlinCodeParser = KotlinCodeParser()
val extractedTags = kotlinCodeParser.extractKDocTags(kdoc, "param")
println(extractedTags) // Prints: {"param":"tag"}
```

#### Notes:
- The `extractKDocTags` method only extracts the specified tag from the KDoc. If you need to extract all tags, you would need to call this method multiple times with different tag names.
- The method is case-sensitive. Ensure that the `tagName` parameter matches exactly with the tag name in the KDoc.
- If the KDoc does not contain the specified tag, the method will not throw an error. Instead, it will return an empty map.
- The method does not validate the KDoc or the tag. It assumes that the KDoc and the tag are valid.

Parameters:
- `kdoc` (KDoc): No description available
- `tagName` (String): No description available

### extractKDocTagsList

---
## Class: KotlinCodeParser
### Method: extractKDocTagsList

#### Description:
The `extractKDocTagsList` method is designed to parse through a given KDoc (Kotlin Documentation) and extract a list of specific tags. This method is useful when you want to retrieve all instances of a particular tag from a KDoc.

#### Parameters:
- `kdoc` (KDoc): The KDoc object that you want to parse. This should be a valid KDoc object.
- `tagName` (String): The name of the tag that you want to extract from the KDoc. This should be a valid tag name.

#### Returns:
This method returns a `List<String>`. Each string in the list represents an instance of the specified tag found in the provided KDoc. If no instances of the tag are found, the method will return an empty list.

#### Usage Example:
```kotlin
val kdoc = KDoc("/** @author John Doe @version 1.0 */")
val kotlinCodeParser = KotlinCodeParser()
val authorTags = kotlinCodeParser.extractKDocTagsList(kdoc, "author")
println(authorTags) // Output: ["John Doe"]
```
In this example, we create a KDoc object with two tags: `@author` and `@version`. We then use the `extractKDocTagsList` method to extract all instances of the `@author` tag.

#### Notes:
- The `extractKDocTagsList` method is case-sensitive. Ensure that the `tagName` parameter matches the case of the tags in the KDoc.
- The method does not validate whether the provided `kdoc` is a valid KDoc object or whether the `tagName` is a valid tag name. It's the responsibility of the caller to provide valid inputs.
- If the same tag appears multiple times in the KDoc, each instance will be included in the returned list.

Parameters:
- `kdoc` (KDoc): No description available
- `tagName` (String): No description available

### extractKDocTag

---
## Class: KotlinCodeParser

### Method: extractKDocTag

#### Description:

The `extractKDocTag` method is a part of the `KotlinCodeParser` class. Its primary purpose is to parse and extract a specific tag from a given KDoc comment.

#### Parameters:

- `kdoc: KDoc` - This parameter represents the KDoc comment from which the tag needs to be extracted. It is expected to be an instance of the KDoc class.
- `tagName: String` - This parameter is a string that specifies the name of the tag to be extracted from the provided KDoc comment.

#### Return Value:

This method returns a `String?` type. The returned string contains the content of the specified tag if it exists in the provided KDoc comment. If the specified tag does not exist, the method will return `null`.

#### Usage Example:

```kotlin
val kdoc = KDoc("/** @author John Doe */")
val parser = KotlinCodeParser()
val authorTag = parser.extractKDocTag(kdoc, "author")
println(authorTag) // Outputs: John Doe
```

In the above example, the `extractKDocTag` method is used to extract the `author` tag from a KDoc comment. The extracted tag content is then printed to the console.

#### Important Notes:

- The `extractKDocTag` method is case-sensitive. Therefore, the `tagName` parameter should match exactly with the tag in the KDoc comment.
- If the KDoc comment does not contain the specified tag, the method will return `null`. Therefore, it is recommended to handle the nullability of the returned value to prevent potential `NullPointerExceptions`.
- The method does not validate the structure or content of the KDoc comment. It is the responsibility of the caller to ensure that the provided KDoc comment is valid.
- The method will only return the first occurrence of the specified tag. If the KDoc comment contains multiple instances of the same tag, subsequent instances will be ignored.

Parameters:
- `kdoc` (KDoc): No description available
- `tagName` (String): No description available

### extractDependencies

---
# KotlinCodeParser Class Documentation

## Method: extractDependencies

### Description

The `extractDependencies` method is a part of the `KotlinCodeParser` class. Its primary purpose is to analyze a Kotlin source file and extract all the dependencies that are present in the file. Dependencies are identified by import statements at the beginning of the source file.

### Parameters

The method takes one parameter:

- `psiFile`: A `KtFile` object representing the Kotlin source file to be analyzed. This file should be a valid Kotlin source file.

### Return Value

The `extractDependencies` method returns a `List<String>`. Each string in the list represents a unique dependency found in the `psiFile`. The dependencies are represented as fully qualified names, i.e., they include the package name and the class name.

### Usage Example

Here is a simple usage example:

```kotlin
val file = KtPsiFactory(project).createFile(text)
val parser = KotlinCodeParser()
val dependencies = parser.extractDependencies(file)

for (dependency in dependencies) {
    println(dependency)
}
```

In this example, a `KtFile` object is created from a string of text. The `extractDependencies` method is then used to find all dependencies in the file. The dependencies are then printed to the console.

### Important Notes

- The `extractDependencies` method only identifies explicit import statements. It does not identify implicit dependencies or dependencies that are not declared with an import statement.
- The method does not handle nested dependencies. If a dependency itself has other dependencies, those are not included in the returned list.
- The method does not check if the dependencies are valid or if they can be resolved. It only extracts the names from the import statements.
- The method does not handle duplicate import statements. If a dependency is imported more than once, it will appear multiple times in the returned list.

Parameters:
- `psiFile` (KtFile): No description available

### extractNestedClasses

---

## Class: KotlinCodeParser

### Method: extractNestedClasses

---

#### Description:

The `extractNestedClasses` method is designed to parse a given Kotlin class and extract all nested classes within it. This method is particularly useful when you need to analyze or manipulate the structure of a Kotlin class programmatically.

#### Parameters:

- `ktClass: KtClass`: This is the input parameter representing the Kotlin class from which the nested classes are to be extracted. The `KtClass` should be an instance of a Kotlin class.

#### Return Value:

This method returns a `List<ClassInfo>`. Each `ClassInfo` object in the list represents a nested class found within the input `ktClass`. The `ClassInfo` object contains all the relevant information about a class, such as its name, methods, properties, etc. If no nested classes are found within the `ktClass`, the method will return an empty list.

#### Usage Example:

```kotlin
val parser = KotlinCodeParser()
val ktClass = KtClass("MyClass") // Assume MyClass is a valid Kotlin class with nested classes
val nestedClasses = parser.extractNestedClasses(ktClass)

for (classInfo in nestedClasses) {
    println(classInfo.name)
}
```
In this example, we first create an instance of `KotlinCodeParser`. Then, we create a `KtClass` instance representing the class we want to analyze. We call `extractNestedClasses` on the parser instance, passing the `ktClass` as the argument. The method returns a list of `ClassInfo` objects, each representing a nested class within `MyClass`. We then loop through the list and print the name of each nested class.

#### Important Notes:

- The `extractNestedClasses` method only extracts direct nested classes. It does not recursively extract nested classes within the nested classes. If you need to extract nested classes recursively, you will need to call this method on each extracted nested class.
- The `KtClass` instance passed as the parameter should represent a valid Kotlin class. If an invalid class is passed, the behavior of the method is undefined.
- The `extractNestedClasses` method does not modify the input `ktClass`. It only analyzes its structure and returns information about its nested classes.

Parameters:
- `ktClass` (KtClass): No description available

### extractCompanionObject

---
## Class: KotlinCodeParser
### Method: extractCompanionObject

#### Description:
The `extractCompanionObject` method is a part of the `KotlinCodeParser` class. Its main purpose is to analyze a given Kotlin class (`ktClass`) and extract information about its companion object, if one exists.

#### Parameters:
- `ktClass: KtClass` - The Kotlin class to be analyzed. This should be an instance of the `KtClass` type.

#### Return Value:
This method returns an instance of `CompanionObjectInfo?`. This object contains information about the companion object of the provided `ktClass`, if it exists. If the `ktClass` does not have a companion object, the method will return `null`.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktClass = KtClass("MyClass")
val companionObjectInfo = kotlinCodeParser.extractCompanionObject(ktClass)
```

In this example, we first create an instance of `KotlinCodeParser`. Then, we create a `KtClass` instance named `MyClass`. We then call the `extractCompanionObject` method on the `kotlinCodeParser` instance, passing `MyClass` as the argument. The method returns information about the companion object of `MyClass`, if it exists.

#### Important Notes:
- The `extractCompanionObject` method only analyzes the immediate companion object of the provided `ktClass`. If the companion object itself has a companion object, this method will not extract information about it.
- If the `ktClass` does not have a companion object, the method will return `null`. Therefore, it is important to handle the potential `null` return value in your code to prevent `NullPointerExceptions`.
- The `extractCompanionObject` method does not modify the provided `ktClass` in any way. It only analyzes and extracts information from it.

Parameters:
- `ktClass` (KtClass): No description available

### extractFieldsFromObject

---
## Class: KotlinCodeParser
### Method: extractFieldsFromObject

#### Description:
The `extractFieldsFromObject` method is used to parse and extract all the fields from a given Kotlin object declaration. This method is a part of the `KotlinCodeParser` class and is primarily used for code analysis, refactoring, and other similar tasks where field information from an object is required.

#### Parameters:
- `obj: KtObjectDeclaration` : This is the input parameter representing a Kotlin object declaration. The method will parse this object and extract all the fields from it.

#### Return Value:
This method returns a `List<FieldInfo>`. Each `FieldInfo` object in the list represents a field in the input Kotlin object declaration. The `FieldInfo` object contains information about the field such as its name, type, visibility, and whether it is mutable or not.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktObjectDeclaration = // assume this is a valid KtObjectDeclaration
val fields = kotlinCodeParser.extractFieldsFromObject(ktObjectDeclaration)
for (field in fields) {
    println("Field Name: ${field.name}, Field Type: ${field.type}")
}
```
In this example, we first create an instance of `KotlinCodeParser`. Then we call the `extractFieldsFromObject` method on this instance, passing a `KtObjectDeclaration` as the parameter. The method returns a list of `FieldInfo` objects which we iterate over to print the name and type of each field.

#### Important Notes:
- The `extractFieldsFromObject` method only extracts fields from the top level of the object. It does not extract fields from nested objects or classes within the input object.
- The method does not handle parsing errors. If the input `KtObjectDeclaration` is not a valid Kotlin object declaration, the behavior of the method is undefined.
- The returned `FieldInfo` objects do not contain information about any annotations that might be present on the fields.

Parameters:
- `obj` (KtObjectDeclaration): No description available

### extractMethodsFromObject

---
Class: KotlinCodeParser

Method: extractMethodsFromObject

Return Type: List<MethodInfo>

Parameters: obj: KtObjectDeclaration

---

**Method Documentation**

**Method Name:** extractMethodsFromObject

**Description:**

The `extractMethodsFromObject` method in the `KotlinCodeParser` class is designed to parse a given Kotlin object declaration and extract all methods contained within it. This method is primarily used for code analysis, refactoring, or other similar tasks where information about the methods of an object is required.

**Parameters:**

- `obj: KtObjectDeclaration` - This parameter represents the Kotlin object declaration from which the methods are to be extracted. It is an instance of `KtObjectDeclaration`, which is a part of the Kotlin PSI (Program Structure Interface) and represents a Kotlin object declaration in the code.

**Return Value:**

This method returns a `List<MethodInfo>`. Each `MethodInfo` object in the list represents a method found within the provided Kotlin object declaration. The `MethodInfo` object contains information about the method, such as its name, parameters, return type, and visibility.

**Usage Example:**

```kotlin
val kotlinCodeParser = KotlinCodeParser()
val ktObjectDeclaration = ... // Obtain a KtObjectDeclaration instance
val methodInfos = kotlinCodeParser.extractMethodsFromObject(ktObjectDeclaration)

for (methodInfo in methodInfos) {
    println("Method name: ${methodInfo.name}")
    println("Return type: ${methodInfo.returnType}")
    // Process other method information as required
}
```

**Important Notes:**

- This method will only extract methods directly declared within the provided object. It will not extract methods from any nested or inner objects, nor will it extract methods inherited from superclasses or interfaces.
- If the provided object does not contain any methods, the method will return an empty list.
- This method does not modify the provided object in any way. It only analyzes and extracts information from it.
- The order of the `MethodInfo` objects in the returned list corresponds to the order of the methods in the source code.

Parameters:
- `obj` (KtObjectDeclaration): No description available

### extractObjects

---
## Class: KotlinCodeParser
### Method: extractObjects

#### Description:
The `extractObjects` method is used to parse a given Kotlin class (`KtClass`) and extract all the objects within it. The objects are returned as a list of `ObjectInfo` instances.

#### Parameters:
- `ktClass` (KtClass): The Kotlin class from which the objects are to be extracted. This should be an instance of the `KtClass` type.

#### Return Value:
This method returns a `List<ObjectInfo>`. Each `ObjectInfo` instance in the list represents an object found within the provided `ktClass`. If no objects are found within the `ktClass`, an empty list is returned.

#### Usage Example:
```kotlin
val parser = KotlinCodeParser()
val ktClass = KtClass("MyClass") // Assume MyClass is a predefined Kotlin class
val objectInfoList = parser.extractObjects(ktClass)

for (info in objectInfoList) {
    println(info.name)
}
```
In this example, we first create an instance of `KotlinCodeParser`. Then we define a `KtClass` instance representing the class we wish to extract objects from. We then call `extractObjects` on our parser instance, passing in our `KtClass` instance. The method returns a list of `ObjectInfo` instances, which we iterate over to print the name of each object.

#### Important Notes:
- The `extractObjects` method only extracts objects that are directly defined within the provided `KtClass`. It does not extract objects that are defined within other objects or classes within the `KtClass`.
- The `extractObjects` method does not extract objects from superclasses of the provided `KtClass`.
- The `extractObjects` method will not modify the provided `KtClass` instance. It only reads data from it.
- If the provided `KtClass` instance is null, the `extractObjects` method will throw a `NullPointerException`.
- The order of `ObjectInfo` instances in the returned list corresponds to the order of objects in the source code of the `KtClass`.

Parameters:
- `ktClass` (KtClass): No description available

### extractAnnotations

---
## Class: KotlinCodeParser

### Method: extractAnnotations

#### Description:
The `extractAnnotations` method is part of the `KotlinCodeParser` class. Its primary function is to parse and extract all annotations from a given Kotlin annotated element.

#### Parameters:
- `element: KtAnnotated`: This parameter represents a Kotlin annotated element from which the method will extract all annotations. The element should be an instance of `KtAnnotated`, which represents any Kotlin element that can be annotated.

#### Return Value:
The method returns a `List<AnnotationInfo>`. This list contains instances of the `AnnotationInfo` class, each of which represents an extracted annotation from the input Kotlin annotated element. Each `AnnotationInfo` instance contains detailed information about a single annotation, such as its name, parameters, and usage context.

#### Usage Example:
```kotlin
val kotlinCodeParser = KotlinCodeParser()
val element: KtAnnotated = ...
val annotations: List<AnnotationInfo> = kotlinCodeParser.extractAnnotations(element)
for (annotation in annotations) {
    println("Annotation: ${annotation.name}")
}
```
In this example, `element` is a Kotlin annotated element. The `extractAnnotations` method is called to extract all annotations from `element`. The method returns a list of `AnnotationInfo` instances, which is then iterated over to print the name of each annotation.

#### Important Notes:
- The `extractAnnotations` method only extracts annotations from the provided `KtAnnotated` element. It does not traverse any child elements that may also be annotated. If you need to extract annotations from a hierarchy of elements, you will need to traverse the hierarchy and call `extractAnnotations` on each `KtAnnotated` element.
- The method will return an empty list if the provided `KtAnnotated` element has no annotations.
- The `extractAnnotations` method does not modify the provided `KtAnnotated` element. It only reads and extracts information from it.
- The order of the `AnnotationInfo` instances in the returned list matches the order of the annotations in the source code.

Parameters:
- `element` (KtAnnotated): No description available

### checkModifier

---
Class: KotlinCodeParser
Method: checkModifier
Return Type: Boolean
Parameters: element: KtModifierListOwner, token: KtModifierKeywordToken

---

## Method Description

The `checkModifier` method of the `KotlinCodeParser` class is used to verify if a specific modifier keyword token is present in a given Kotlin element. The element should be an instance of `KtModifierListOwner`, which represents a Kotlin element that can own a modifier list. The token should be an instance of `KtModifierKeywordToken`, representing a modifier keyword in Kotlin.

## Parameters

- `element` (`KtModifierListOwner`): This parameter represents the Kotlin element that is being checked for the presence of a specific modifier. It should be an instance of `KtModifierListOwner`.

- `token` (`KtModifierKeywordToken`): This parameter represents the modifier keyword token that is being checked for within the provided Kotlin element. It should be an instance of `KtModifierKeywordToken`.

## Return Value

This method returns a `Boolean` value. If the specified modifier keyword token is found within the provided Kotlin element, the method will return `true`. If the modifier keyword token is not found, the method will return `false`.

## Usage Example

```kotlin
val kotlinCodeParser = KotlinCodeParser()
val element: KtModifierListOwner = ...
val token: KtModifierKeywordToken = KtTokens.PUBLIC_KEYWORD

if (kotlinCodeParser.checkModifier(element, token)) {
    println("The element contains the 'public' modifier.")
} else {
    println("The element does not contain the 'public' modifier.")
}
```

## Important Notes

- The `checkModifier` method only checks for the presence of a single modifier keyword token within the provided Kotlin element. If you need to check for multiple modifiers, you will need to call this method multiple times, once for each modifier.

- The method does not check for the validity of the modifier in the context of the provided Kotlin element. It only checks for the presence of the modifier. Therefore, the presence of a modifier does not necessarily mean that it is being used correctly in the context of the Kotlin element.

Parameters:
- `element` (KtModifierListOwner): No description available
- `token` (KtModifierKeywordToken): No description available

### getContent

---
## Class: KotlinCodeParser
### Method: getContent

#### Description:
The `getContent` method is a function within the `KotlinCodeParser` class. The purpose of this method is to parse the Kotlin code and retrieve the content of the code as a string.

#### Parameters:
This method does not take any parameters.

#### Return Value:
This method returns a `String` which represents the content of the parsed Kotlin code. The returned string includes all the code lines, comments, and white spaces present in the original Kotlin code.

#### Usage Example:
```kotlin
val parser = KotlinCodeParser()
val content = parser.getContent()
println(content)
```
In the above example, an instance of `KotlinCodeParser` is created. Then, the `getContent` method is called to retrieve the content of the Kotlin code. The content is then printed to the console.

#### Important Notes:
- The `getContent` method does not modify the original Kotlin code in any way. It only retrieves the content as a string.
- If the Kotlin code is empty or null, the `getContent` method will return an empty string.
- This method does not handle any exceptions. If the Kotlin code contains any syntax errors, the method may fail or return incorrect results. It is recommended to validate the Kotlin code before calling this method.
- The `getContent` method does not ignore comments or white spaces. They are included in the returned string.
