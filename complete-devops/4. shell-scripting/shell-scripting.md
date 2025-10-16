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

---

# ⚙️ Functions in Shell Script

## 📘 What is a Function?

A **function** in shell scripting is a **block of reusable code** that performs a specific task.  
Functions make scripts modular, readable, and easier to maintain. They can be called directly by name.

---

## 🔹 Function Syntax

### ✅ **Two common ways to define a function:**

```bash
# Method 1
function function_name() {
    commands
}

# Method 2 (recommended)
function_name() {
    commands
}

# Example
greet() {
    echo "Hello, $1! Welcome to Shell Scripting."
}

greet "Nancy"
```
## Explanation:
- $1 → Refers to the first argument passed to the function.
- You can pass multiple arguments like $2, $3, etc.

## 🔹Function Return Values

Functions can return:
- Exit status codes (0–255) using return
- Output text or numbers using echo

Example (using return):
```bash
is_even() {
    if (( $1 % 2 == 0 )); then
        return 0     # success (true)
    else
        return 1     # failure (false)
    fi
}

is_even 6
if [ $? -eq 0 ]; then
    echo "Number is even"
else
    echo "Number is odd"
fi
```
## Explanation:
- $? stores the exit status of the last executed command.
- 0 → success/true, 1 → failure/false.


## Variable Scope in Functions
- By default, all variables in shell are global.
- To make a variable local (only accessible inside the function), use the local keyword.

```bash
display_count() {
    local count=5
    echo "Inside function: count = $count"
}

count=10
display_count
echo "Outside function: count = $count"
```

Output -
```bash
Inside function: count = 5
Outside function: count = 10
```

## Summary
| Concept          | Keyword/Usage             | Description                              |
| ---------------- | ------------------------- | ---------------------------------------- |
| Define function  | `function_name() { ... }` | Declares a function                      |
| Pass arguments   | `$1`, `$2`, `$3`          | Positional parameters                    |
| Return value     | `return <code>`           | Returns exit status (0–255)              |
| Output text      | `echo`                    | Print to stdout (can capture via `$( )`) |
| Local variable   | `local var=value`         | Limits variable scope                    |
| Access exit code | `$?`                      | Gets last command’s return code          |


# 🧰 Understanding `/dev/null` and `2>&1`

## 📘 1. What is `/dev/null`?

`/dev/null` is a **special device file** in Linux/Unix systems that **discards all data** written to it.

Think of it as a **“black hole”** for unwanted output — anything sent to `/dev/null` disappears forever.

### 🧠 Meaning:
> `/dev/null` = “Nothingness” — it accepts any input and produces no output.

### Example:
```bash
echo "Hello World" > /dev/null
```
This command executes successfully but prints nothing because output is discarded.

## Standard Output and Standard Error
Every command in Linux has three standard I/O streams:
| Stream   | Description     | File Descriptor | Default Destination |
| -------- | --------------- | --------------- | ------------------- |
| `stdin`  | Standard Input  | `0`             | Keyboard            |
| `stdout` | Standard Output | `1`             | Screen              |
| `stderr` | Standard Error  | `2`             | Screen              |

```bash
ls /home             # user  documents  downloads                                  -> stdout (1)
ls /wrong/path       # ls: cannot access '/wrong/path': No such file or directory  -> stderr (2)

# You can redirect unwanted output to /dev/null.
ls /home > /dev/null


# Discard Both Output and Error:
ls /home /wrong/path > /dev/null 2>&1

# ✅ Hides everything (no output, no error)
```

## Understanding 2>&1
| Symbol | Meaning                                |
| ------ | -------------------------------------- |
| `>`    | Redirect output                        |
| `2>`   | Redirect standard error (stderr)       |
| `&1`   | Refers to file descriptor `1` (stdout) |

➡️ “Send standard error (2) to the same place as standard output (1).”

## Logging 
```bash
#!/bin/bash
# basic_logging.sh

echo "Script started at $(date)" > script.log
echo "Running connectivity check..." >> script.log

ping -c 2 google.com >> script.log 2>&1

echo "Script ended at $(date)" >> script.log
```

### Logging with tee Command
**tee allows you to display output on the screen while saving it to a file.**
```bash
#!/bin/bash
# tee_logging.sh

LOGFILE="system_$(date +%F).log"

echo "=== System Info Log Started at $(date) ===" | tee -a "$LOGFILE"
```


# ⏰ Automating Scripts Using `at` Command
Step 1: create a script 
```bash
#!/bin/bash
# backup.sh

echo "Backup started at $(date)" >> ~/backup.log
tar -czf ~/Documents_backup_$(date +%F).tar.gz ~/Documents
echo "Backup completed at $(date)" >> ~/backup.log
```

Step 2: make it executable
```bash
chmod +x backup.sh
```

Step 3: Schedule it using at
```bash
at 11:00 PM -f backup.sh
```
✅ This will run backup.sh automatically at 11:00 PM.

List all scheduled jobs:
```bash
atq

# Remove a specific job:
atrm <job_id>
```

# 🕒 Automating Scripts Using `cron` (Crontab)

## 📘 1. What is `cron`?

`cron` is a Linux **job scheduler** that automatically runs commands or scripts **at fixed intervals** —  
such as every minute, hour, day, week, or month.

It is ideal for **repetitive automation tasks** like:
- Regular backups  
- Log cleanups  
- Health checks  
- Syncing or monitoring scripts  

---

## 🔹 2. The `crontab` Command

`crontab` (CRON table) is the configuration file that defines **scheduled tasks**.

### Basic Commands:

| Command | Description |
|----------|-------------|
| `crontab -e` | Edit the cron jobs for the current user |
| `crontab -l` | List your scheduled cron jobs |
| `crontab -r` | Remove all scheduled jobs for current user |
| `sudo crontab -e` | Edit root user's cron jobs |

---

## 🔹 3. Crontab File Format

Each line in a crontab file represents one scheduled job, defined as:
<img width="656" height="347" alt="image" src="https://github.com/user-attachments/assets/91f2dd1e-2976-4303-b479-05f9859b9328" />


---

## 🔹 4. Crontab Time Examples

| Expression | Description |
|-------------|--------------|
| `* * * * *` | Every minute |
| `0 * * * *` | Every hour |
| `0 0 * * *` | Every day at midnight |
| `0 6 * * *` | Every day at 6 AM |
| `*/10 * * * *` | Every 10 minutes |
| `0 0 * * 0` | Every Sunday at midnight |
| `0 9-17 * * 1-5` | Every hour from 9 AM–5 PM, Mon–Fri |
| `0 0 1 * *` | On the first day of every month |

---

## 🔹 5. Scheduling a Shell Script

### Example Script — `backup.sh`
```bash
#!/bin/bash
# backup.sh

SOURCE="/home/nancy/Documents"
DEST="/home/nancy/backup"
LOGFILE="/home/nancy/backup/backup_$(date +%F).log"

echo "==== Backup started at $(date) ====" >> "$LOGFILE"
tar -czf "$DEST/Documents_$(date +%F).tar.gz" "$SOURCE" >> "$LOGFILE" 2>&1
echo "==== Backup completed at $(date) ====" >> "$LOGFILE"
```

Make it executable:
```bash
chmod +x ~/backup.sh
```

Schedule it using crontab:
```bash
crontab -e
```

Add the line:
```bash
0 2 * * * /home/nancy/backup.sh
```

✅ This runs backup.sh daily at 2:00 AM.


