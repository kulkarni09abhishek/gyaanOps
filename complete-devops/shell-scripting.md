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

---

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

---

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


# 🔢 Arithmetic Operators
Arithmetic operators are used to perform **mathematical calculations** such as addition, subtraction, multiplication, division, and modulus on numeric values in shell scripts.

Bash supports arithmetic in **multiple ways**, such as:
- `expr` command  
- `$(( ))` arithmetic expansion (recommended)  
- `let` command  

---

## 🧱 Basic Arithmetic Operators

| Operator | Description | Example |
|-----------|--------------|----------|
| `+` | Addition | `a + b` |
| `-` | Subtraction | `a - b` |
| `*` | Multiplication | `a * b` |
| `/` | Division | `a / b` |
| `%` | Modulus (remainder) | `a % b` |
| `**` | Exponentiation (Bash 4+) | `a ** b` |

---

## Using $(( )) (Arithmetic Expansion) — Recommended
```bash
a=10
b=3

echo "Sum: $((a + b))"
echo "Difference: $((a - b))"
echo "Product: $((a * b))"
echo "Division: $((a / b))"
echo "Modulus: $((a % b))"
echo "Power: $((a ** b))"
```

## 🧩 Using let Command
let allows performing arithmetic operations without using $(( )).
```bash
let a=10
let b=5
let sum=a+b
let prod=a*b

echo "Sum: $sum"
echo "Product: $prod"
```


# ⚙️ Conditional Statements

## 🧩 Types of Conditional Statements
1. **if statement**  
2. **if-else statement**  
3. **if-elif-else ladder**  
4. **nested if statements**  
5. **case statement**

## 🔹`if` Statement

### Syntax:
```bash
if [ condition ]
then
    command1
    command2
fi


# Example 1
num=10
if [ $num -gt 5 ]
then
    echo "Number is greater than 5"
fi

# Example 2
age=18
if [ $age -ge 18 ]
then
    echo "Eligible to vote"
else
    echo "Not eligible to vote"
fi

# Example 3
a=8
b=12
if [ $a -gt 5 ] && [ $b -lt 15 ]
then
    echo "Both conditions are true"
fi

```

## 🔹File Test Conditions

| Test      | Description                    | Example              |
| --------- | ------------------------------ | -------------------- |
| `-e file` | True if file exists            | `[ -e file.txt ]`    |
| `-f file` | True if file is a regular file | `[ -f file.txt ]`    |
| `-d dir`  | True if directory exists       | `[ -d /home/nancy ]` |
| `-r file` | True if readable               | `[ -r file.txt ]`    |
| `-w file` | True if writable               | `[ -w file.txt ]`    |
| `-x file` | True if executable             | `[ -x script.sh ]`   |
| `-s file` | True if file not empty         | `[ -s file.txt ]`    |


```bash
file="data.txt"
if [ -f "$file" ]
then
    echo "File exists"
else
    echo "File does not exist"
fi
```

## Case 
```bash
echo "Enter a number between 1 to 3:"
read num

case $num in
    1)
        echo "You chose One" ;;
    2)
        echo "You chose Two" ;;
    3)
        echo "You chose Three" ;;
    *)
        echo "Invalid choice" ;;
esac
```

✅ Summary:
- Use [ condition ] for simple tests, [[ ]] for advanced conditions.
- Combine conditions with &&, ||, and !.
- case is ideal for multi-choice logic.
- Always end if with fi and case with esac.



# 🔁 Loops in Shell Scripting
## 🔹 2. Types of Loops in Shell

1. **for loop** — iterate over a list of items  
2. **while loop** — run while a condition is true  
3. **until loop** — run until a condition becomes true  
4. **nested loops** — loops inside loops  

---

## 🔁 `for` Loop

### Syntax 1 — Iterating over a List
```bash
for variable in list
do
    command(s)
done

# Example
for fruit in Apple Banana Mango Orange
do
    echo "Fruit: $fruit"
done

# Looping Over Command Output
for item in $(ls)
do
    echo "File: $item"
done

# while Loop
while [ condition ]
do
    command(s)
done

# Example
count=1
while [ $count -le 5 ]
do
    echo "Count: $count"
    ((count++))
done

# Reading a File Line by Line
while read line
do
    echo "Line: $line"
done < myfile.txt

```


# ⚖️ Logical and Relational Operators
## Relational Operators (Numeric Comparisons)

Used to compare **numbers** inside `[ ]` or `(( ))`.

| Operator | Meaning | Example | Description |
|-----------|----------|----------|--------------|
| `-eq` | Equal to | `[ $a -eq $b ]` | True if `$a` equals `$b` |
| `-ne` | Not equal to | `[ $a -ne $b ]` | True if `$a` is not equal to `$b` |
| `-gt` | Greater than | `[ $a -gt $b ]` | True if `$a` is greater than `$b` |
| `-lt` | Less than | `[ $a -lt $b ]` | True if `$a` is less than `$b` |
| `-ge` | Greater than or equal to | `[ $a -ge $b ]` | True if `$a` is greater than or equal to `$b` |
| `-le` | Less than or equal to | `[ $a -le $b ]` | True if `$a` is less than or equal to `$b` |

## Relational Operators with (( ))
When using arithmetic expressions, you can use standard mathematical operators instead of -eq, -gt, etc.
| Operator | Meaning                  | Example        |
| -------- | ------------------------ | -------------- |
| `==`     | Equal to                 | `(( a == b ))` |
| `!=`     | Not equal to             | `(( a != b ))` |
| `<`      | Less than                | `(( a < b ))`  |
| `>`      | Greater than             | `(( a > b ))`  |
| `<=`     | Less than or equal to    | `(( a <= b ))` |
| `>=`     | Greater than or equal to | `(( a >= b ))` |

## Logical Operators
Used to combine multiple conditions in a single test.
| Operator | Description                                   | Example                       | Meaning                    | 
| -------- | --------------------------------------------- | ----------------------------- | -------------------------- | 
| `-a`     | Logical AND                                   | `[ $a -gt 0 -a $b -gt 0 ]`    | True if both are true      | 
| `-o`     | Logical OR                                    | `[ $a -eq 0 -o $b -gt 0 ]`    | True if either is true     | 
| `!`      | Logical NOT                                   | `[ ! -f file.txt ]`           | True if condition is false | 
| `&&`     | AND (modern, preferred in `[[ ]]` or `(( ))`) | `[[ $a -gt 5 && $b -lt 10 ]]` | True if both true          | 
| `        |                                               | `                             | OR (modern, preferred)     | 


```bash
# Example
a=8
b=12

if [[ $a -gt 5 && $b -lt 15 ]]
then
    echo "Both conditions are true"
fi

if [[ $a -lt 5 || $b -gt 10 ]]
then
    echo "At least one condition is true"
fi
```











