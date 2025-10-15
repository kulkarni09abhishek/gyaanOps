# Shell Scripting Notes

---


# 🐚 Variables

## 📘 1. What are Variables?
Variables in shell scripting are used to **store data values** like strings, numbers, filenames, etc.  
They allow you to **reuse** values and make your scripts **dynamic and flexible**.

---

## 🧱 2. Declaring Variables

### Syntax:
```bash
variable_name=value
```

⚠️ Note: No spaces before or after = sign.


## 💬 3. Accessing Variables

```bash
name="Nancy"
age=27

echo "My name is $name and I am $age years old."
```


## 🧮 4. Using Command Substitution

```bash
current_date=$(date)
echo "Today's date is: $current_date"
```

## 🔠 5. Read User Input
You can assign user input to a variable using read.

```bash
echo "Enter your name:"
read username
echo "Hello, $username!"
```

## 🧩 6. Environment Variables
Environment variables are global and available to all programs.
Common examples: $USER, $HOME, $PATH, $PWD.

```bash
echo "Current User: $USER"
echo "Home Directory: $HOME"
```

## 🧹 7. Unsetting Variables
To delete a variable from memory:

```bash
unset variable_name
```


## 🔒 8. Constant Variables (Read-only)
To delete a variable from memory:

```bash
readonly variable_name

readonly pi=3.14159
pi=3.14  # ❌ Error: pi: is read-only variable
```

##  9. Special Variables

| Variable        | Description                           |
| --------------- | ------------------------------------- |
| `$0`            | Script name                           |
| `$1`, `$2`, ... | Command-line arguments                |
| `$#`            | Number of arguments                   |
| `$@`            | All arguments                         |
| `$?`            | Exit status of last command           |
| `$$`            | Process ID of current shell           |
| `$!`            | Process ID of last background command |



# 🧮 Arrays
Arrays are used to store **multiple values** in a **single variable**.  
Each element in an array is identified by its **index number**, starting from **0**.

---

## Declaring Arrays

### Syntax:
```bash
array_name=(value1 value2 value3 ...)

fruits=("Apple" "Banana" "Mango" "Orange")
```

## Array Elements operations
```bash
${array_name[index]}

echo ${fruits[0]}   # Apple
echo ${fruits[2]}   # Mango

# Accessing All Elements
${array_name[@]}
# or
${array_name[*]}

echo "All fruits: ${fruits[@]}"

# Getting Array Length
${#array_name[@]}

echo "Number of fruits: ${#fruits[@]}"

# Adding Elements to Array
fruits[4]="Grapes"
echo ${fruits[@]}

# Removing Elements
unset array_name[index]

unset fruits[1]   # Removes "Banana"
echo ${fruits[@]}

# Slice array
${array[@]:start:length}

# Looping Through Arrays
for fruit in "${fruits[@]}"
do
  echo "Fruit: $fruit"
done
```


## Associative Arrays
- Indexed by string keys
- Available in Bash 4+

```bash
declare -A person
person[name]="Nancy"
person[age]=27
person[city]="Pune"

echo "Name: ${person[name]}"
echo "Age: ${person[age]}"
echo "City: ${person[city]}"
```


# 🧵 String Operations

## 📘 1. What are Strings?
Strings in shell scripting are sequences of characters enclosed in **single quotes ('')** or **double quotes ("")**.

---

## 🧱 2. Declaring Strings

### Syntax:
```bash
variable_name="string_value"

# example
name="Nancy Wheeler"
greeting='Welcome to Shell Scripting!'
```

✅ Use double quotes if your string contains variables or commands that need expansion.
❌ Single quotes preserve the literal value (no variable or command substitution).


## String operations
```bash
# String Concatenation
first_name="Nancy"
last_name="Wheeler"
full_name="$first_name $last_name"
echo "Full Name: $full_name"

# String Length
name="Nancy"
echo "Length of name: ${#name}"

# Extracting Substrings
message="ShellScripting"
echo ${message:0:5}   # Shell
echo ${message:5}     # Scripting

# Removing Substrings
${variable#pattern}   # Removes shortest match from start
${variable##pattern}  # Removes longest match from start

filename="backup_2025.tar.gz"
echo ${filename#*_}    # 2025.tar.gz
echo ${filename##*_}   # tar.gz


${variable%pattern}   # Removes shortest match from end
${variable%%pattern}  # Removes longest match from end

echo ${filename%.tar.gz}   # backup_2025


# Replacing Substrings
${variable/pattern/replacement}   # Replace first match
${variable//pattern/replacement}  # Replace all matches

text="I love Java. Java is powerful."
echo ${text/Java/Python}   # Replace first    -> I love Python. Java is powerful.
echo ${text//Java/Python}  # Replace all      -> I love Python. Python is powerful.

# String Comparison
[ string1 = string2 ]     # True if equal
[ string1 != string2 ]    # True if not equal
[ -z string ]             # True if string is empty
[ -n string ]             # True if string is not empty

str1="hello"
str2="world"

if [ "$str1" = "$str2" ]; then
    echo "Strings are equal"
else
    echo "Strings are not equal"
fi


# Case Conversion
word="Hello"
echo ${word,,}   # lowercase → hello
echo ${word^^}   # uppercase → HELLO


# Escape Characters and Quoting
echo "Path: $HOME"      # Prints: Path: /home/nancy
echo 'Path: $HOME'      # Prints: Path: $HOME
```



















