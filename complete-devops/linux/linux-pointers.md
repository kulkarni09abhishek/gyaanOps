# Linux Essentials for DevOps Engineers

---

## How Internet Works

When you connect to Wi-Fi or mobile data, you're connecting through an Internet Service Provider (ISP) like Comcast, AT&T, or Vodafone. They link you to the rest of the internet.

**Request Flow:**

1. Your device sends a request to a DNS server to find out the IP address of a website (IP = street address of the website).
2. The request goes to the server hosting the website.
3. Data is broken into small chunks called **packets** that travel across cables, routers, and switches, possibly taking different routes.
4. The server sends the website data back; your browser reassembles it.

> Most internet traffic, especially international, travels through undersea fiber-optic cables. Satellites are used but are slower.

---

## What is a Server?

A server provides resources or services to clients.

**Example:**

* Email server = serves email.

---

## What is Linux and Why It’s Widely Used as Application Server?

* **Unix:** Paid OS (like macOS)
* **Linux:** Open source, free (GPL license)
* **Windows:** Commercial license

---

## Accessing Remote Machines

| Protocol | Port | Purpose                 |
| -------- | ---- | ----------------------- |
| RDP      | 3389 | Remote Desktop Protocol |
| SSH      | 22   | Secure Shell            |

---

## Linux Architecture

```
Application -> Shell -> Kernel -> Hardware
```

* **Kernel:** Heart of Linux OS
* **Shell:** Gateway between user and kernel; executes shell commands
* **Bootloader (GRUB):** Loads OS into memory (GRUB = Grand Unified Bootloader)

---

## Linux System Info Commands

| Command | Purpose                               |
| ------- | ------------------------------------- |
| top     | Check CPU usage and running processes |
| df -h   | Check storage usage                   |
| free -h | Check memory (RAM) usage              |

---

## Linux File System Overview

| Directory | Description                                |
| --------- | ------------------------------------------ |
| /         | Root directory                             |
| /bin      | Essential command binaries (ls, cp)        |
| /boot     | Bootloader files (Linux kernel)            |
| /dev      | Device files (hard drives, USBs)           |
| /etc      | System configuration files                 |
| /home     | User home directories                      |
| /lib      | Shared libraries for binaries              |
| /media    | Mount point for external devices           |
| /mnt      | Temporary mount point                      |
| /opt      | Optional software packages                 |
| /proc     | Virtual filesystem for process/system info |
| /root     | Root user home directory                   |
| /sbin     | System binaries for admin                  |
| /tmp      | Temporary files (deleted on reboot)        |
| /usr      | Secondary hierarchy for user apps          |
| /var      | Variable data (logs, mail, databases)      |

---

## Key Concepts

* Everything is treated as a file (including devices and processes)
* Linux is case-sensitive (File.txt ≠ file.txt)
* File permissions control access (read/write/execute)
* Mounting makes a filesystem accessible at a directory
* `/etc/passwd` stores user account info; one line per user
* `/etc/environment` stores environment variables (`VAR=value`), view with `env` or `printenv`

---

## Format of /etc/passwd

```
username:x:UID:GID:comment:home_directory:shell
```

**Fields:**

1. username: Login name
2. x: Placeholder for password (real password in /etc/shadow)
3. UID: User ID (0=root)
4. GID: Primary group ID
5. comment: Full name/description (optional)
6. home_directory: Path to home folder
7. shell: Default shell (e.g., /bin/bash)

---

## Linux File Permissions

**Each file/directory has permissions:** Who can read (r), write (w), execute (x)

**Permission format example:**

```
-rwxr-xr--
```

* 1st char: type (- = file, d = directory)
* Next 9 chars: permissions in 3 groups (owner, group, others)

**Letters Meaning:**

* r(4) = read
* w(2) = write
* x(1) = execute

**Changing Permissions:**

```bash
chmod u+x file.txt   # add execute to owner
chmod g-w file.txt   # remove write from group
chmod o=r file.txt   # others read-only
chmod 755            # common for scripts
chmod 644            # regular files
chmod 700            # private files
```

---

## Hard Link vs Soft Link

**Hard Link:**

* Direct pointer to file data
* Data remains even if original is deleted
* Example:

```bash
touch file.txt
ln file.txt hardlink.txt
```

**Soft Link (Symbolic Link):**

* Points to file path (shortcut)
* Broken if original file is deleted
* Can link directories and across filesystems
* Example:

```bash
ln -s file.txt symlink.txt
```

> Use soft links for flexibility, hard links for redundancy.

---

## Some Important Linux Commands

| Command | Purpose                          |
| ------- | -------------------------------- |
| zcat    | View content of compressed files |
| wc      | Count lines, words, bytes        |
| cut     | Extract columns or characters    |

**Common Options for cut:**

* `-d` => delimiter (default TAB)
* `-f` => fields (columns)
* `-c` => characters (positions)

**Examples:**

```bash
# Extract characters 1-5
cut -c1-5 file.txt

# Extract 1st and 3rd characters
cut -c1,3 file.txt

# Extract first column from CSV
cut -d',' -f1 file.csv

# Extract multiple columns
cut -d',' -f1,3 file.csv

# Extract usernames from /etc/passwd
cut -d':' -f1 /etc/passwd

# Extract 2nd column from tab-separated file
cut -f2 file.tsv

# Pipeline example
cat file.txt | cut -d',' -f2
```


# Linux Text Processing and File Commands

---

## AWK Command - Powerful Text Processor

### Basic Syntax

```bash
awk 'pattern {action}' filename
```

### Common Examples

1. Print entire file:

```bash
awk '{print}' file.txt
```

2. Print 1st column:

```bash
awk '{print $1}' file.txt
```

3. Print 1st and 3rd column:

```bash
awk '{print $1, $3}' file.txt
```

4. Use custom delimiter (e.g., comma):

```bash
awk -F',' '{print $2}' file.csv
```

5. Print lines where 2nd column > 50:

```bash
awk '$2 > 50' file.txt
```

6. Pattern match (like grep):

```bash
awk '/error/' file.log
```

---

## SED Command - Stream Editor

### Basic Syntax

```bash
sed [options] 'command' filename
```

### Common Examples

1. Replace "old" with "new" (first match only):

```bash
sed 's/old/new/' file.txt
```

2. Replace all matches on each line:

```bash
sed 's/old/new/g' file.txt
```

3. Delete lines containing "error":

```bash
sed '/error/d' file.txt
```

4. Print only lines 2 to 4:

```bash
sed -n '2,4p' file.txt
```

5. Remove blank lines:

```bash
sed '/^$/d' file.txt
```

---

## GREP Command - Search Text

### Basic Syntax

```bash
grep [options] pattern filename
```

### Common Examples

1. Find lines containing "error":

```bash
grep "error" file.txt
```

2. Case-insensitive search:

```bash
grep -i "error" file.txt
```

3. Show line numbers:

```bash
grep -n "error" file.txt
```

4. Show lines NOT matching:

```bash
grep -v "error" file.txt
```

5. Search recursively in directory:

```bash
grep -r "keyword" /path/to/dir
```

6. Use regex (lines starting with "a"):

```bash
grep '^a' file.txt
```

---

## FIND Command - Search Files and Directories

### Basic Syntax

```bash
find [path] [options] [expression]
```

### Common Examples

1. Find all files in current directory:

```bash
find .
```

2. Find files with specific name:

```bash
find . -name "file.txt"
```

3. Find files ignoring case:

```bash
find . -iname "file.txt"
```

4. Find all directories:

```bash
find . -type d
```

5. Find all `.txt` files:

```bash
find . -type f -name "*.txt"
```

6. Find files modified in last 1 day:

```bash
find . -mtime -1
```

7. Find empty files:

```bash
find . -type f -empty
```

8. Find and delete `.tmp` files:

```bash
find . -name "*.tmp" -delete
```

---

## XARGS Command - Build and Execute Commands

### Basic Syntax

```bash
command | xargs [command]
```

### Common Examples

1. Delete files listed by `find`:

```bash
find . -name "*.log" | xargs rm
```

2. Count lines in multiple `.txt` files:

```bash
find . -name "*.txt" | xargs wc -l
```

3. Copy files listed in a text file:

```bash
cat files.txt | xargs cp -t /backup/
```

4. Run command for each argument:

```bash
echo "file1.txt file2.txt" | xargs -n 1 ls -l
```

5. Use placeholder `{}` with `-I`:

```bash
cat list.txt | xargs -I {} cp {} /backup/
```

---

## SORT Command - Sort Lines in Text Files

### Basic Syntax

```bash
sort [options] [file]
```

### Common Examples

1. Sort a file alphabetically:

```bash
sort file.txt
```

2. Sort in reverse order:

```bash
sort -r file.txt
```

3. Sort by numeric value:

```bash
sort -n numbers.txt
```

4. Sort by 2nd column (space-separated):

```bash
sort -k 2 file.txt
```

5. Sort a CSV by 3rd field:

```bash
sort -t',' -k3 file.csv
```

6. Remove duplicate lines:

```bash
sort file.txt | uniq
```

7. Sort and save to new file:

```bash
sort file.txt > sorted.txt
```

---

## Other Useful Commands

* `diff file1 file2` => Compare two files
* `kill` command => Get PID using `top`, then run:

```bash
kill -9 <pid>
```


# Linux User Management & Networking Commands

---

## User Management Commands

### Basic Commands

* `uname` → Returns platform (e.g., "Linux" on RedHat)
* `who` → Lists users and login details
* `id` → Displays UID, GID, groups

```
Sample output:
uid=0(root) gid=0(root) groups=0(root) context=unconfined_u:unconfined_r:unconfined_t:s0-s0:c0.c1023
```

### What is `sudo`?

* **SuperUser DO**, allows running commands as root or another user.
* Controlled via `/etc/sudoers` file.
* Use `visudo` to edit safely:

```bash
sudo visudo
```

* Add a user to sudo group:

```bash
sudo usermod -aG sudo username
```

### User Management Commands

* Create new user interactively:

```bash
sudo adduser username
```

* Add user without prompt:

```bash
sudo useradd username
sudo passwd username
```

* Delete a user:

```bash
sudo deluser username
```

* Delete user and home directory:

```bash
sudo deluser --remove-home username
```

* Modify user (change username):

```bash
sudo usermod -l newname oldname
```

* Change user’s home directory:

```bash
sudo usermod -d /new/home username
```

* Lock/unlock user account:

```bash
sudo usermod -L username
sudo usermod -U username
```

### Group Management

* Create a group:

```bash
sudo addgroup groupname
```

* Add user to group:

```bash
sudo usermod -aG groupname username
```

* Remove user from group:

```bash
sudo gpasswd -d username groupname
```

* See groups of a user:

```bash
groups username
```

* Set primary group:

```bash
sudo usermod -g groupname username
```

### Switching Users

* Switch to another user:

```bash
su - username
```

* Run a command as another user:

```bash
sudo -u username command
```

### Password Management

* Change your own password:

```bash
passwd
```

* Change another user's password:

```bash
sudo passwd username
```

### User Info & Files

* List all users:

```bash
cat /etc/passwd
```

* List all groups:

```bash
cat /etc/group
```

* Check user's home directory and shell:

```bash
grep username /etc/passwd
```

* Check if user exists:

```bash
id username
```

### Default User Settings

* Default files for new users: `/etc/skel/`
* Default shell and home settings: `/etc/default/useradd`

### Umask

* **User File Creation Mask**, controls default permissions for new files/directories.
* Set permanently for a user: add to `~/.bashrc` or `~/.profile`

```bash
umask 027
```

* System-wide: edit `/etc/profile` or `/etc/login.defs`
* Common values:

  * 002 → Files: 664, Dirs: 775 (shared groups)
  * 022 → Files: 644, Dirs: 755 (default)
  * 027 → Files: 640, Dirs: 750 (secure)

---

## Linux Networking Commands

1. Check IP Address:

```bash
ip a
# or older systems: ifconfig
```

2. Display Routing Table:

```bash
ip route
# or route -n
```

3. Test Network Connectivity:

```bash
ping google.com
ping -c 4 8.8.8.8
```

4. Check DNS Resolution:

```bash
nslookup example.com
# or dig example.com
```

5. Show Open Ports & Listening Services:

```bash
netstat -tuln   # old
ss -tuln        # new
```

6. Display Current Connections:

```bash
netstat -an
ss -an
```

7. Show Network Statistics:

```bash
netstat -s
```

8. Trace Route to a Host:

```bash
traceroute google.com
# or tracepath google.com
```

9. Download a File:

```bash
wget http://example.com/file.zip
curl -O http://example.com/file.zip
```

10. Check Active Network Interfaces:

```bash
ip link show
# or ifconfig -a
```

11. Trace Network Path:

```bash
tracepath google.com
```

12. Whois - Domain info:

```bash
whois google.com
```

13. Nmap - Scan networks, discover hosts/services/ports/OS.
14. Route - Display/change system's IP routing table.

### Loopback IP

* IP: `127.0.0.1`, refers to local machine.
* Hostname: `localhost`
* Reserved range: 127.0.0.0 to 127.255.255.255
* Packets never leave your computer, used for testing.
* Hosts file mapping: `/etc/hosts` → `127.0.0.1 localhost`

### Telnet

* Connect to remote systems via Telnet protocol (TCP), mainly for testing ports.

```bash
telnet [hostname or IP] [port]
```

* Port 80 vs 443:
  | Port | Protocol | Secure? | URL Format |
  |------|----------|---------|------------|
  | 80   | HTTP     | ❌ No   | http://    |
  | 443  | HTTPS    | ✅ Yes  | https://   |

### Open a Port in Linux

* Depends on firewall:

  * `ufw` (Ubuntu)
  * `firewalld` (RHEL/CentOS/Fedora)
  * `iptables` (older/manual)

```bash
sudo ufw allow 8080/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --list-ports
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
```


# Volume Management

## Swap Memory

### 1. What is Swap Memory?

* Swap memory is a space on the disk that is used when the physical RAM (Random Access Memory) is full.
* When the system runs out of RAM, inactive pages in memory are moved to the swap space to free up RAM for active processes.
* Swap is slower than RAM because it's located on the disk.

### 2. Why is it Important?

* Prevents system crashes due to low memory.
* Useful for systems with limited physical RAM.
* Enables hibernation (in some Linux systems).

## Mount vs Attach

### 1. ATTACH (Hardware Level)

* "Attach" means connecting a storage device (like a disk) to a **virtual machine** or **host**.
* It’s like plugging a USB drive into your computer.
* The OS now knows the disk is present, but can’t use it yet.

**Example in Cloud (AWS):**

* Attaching an EBS volume to an EC2 instance.

**Commands:**

```bash
lsblk     # See if the new disk is attached
fdisk -l  # Lists attached disks
```

### 2. MOUNT (File System Level)

* "Mount" means making the **file system** of the attached disk available under a directory.
* Without mounting, even an attached disk is unusable by the OS.
* You "mount" a disk to a folder (e.g., /mnt/data).

**Linux Example:**

```bash
mkdir /mnt/data
mount /dev/sdb1 /mnt/data
```

| Term   | Level         | What it Does                      |
| ------ | ------------- | --------------------------------- |
| Attach | Hardware/VM   | Connects storage to a machine     |
| Mount  | OS/FileSystem | Makes the storage usable in Linux |

## LVM (Logical Volume Management)

### 1. What is LVM?

* LVM is a flexible disk management system.
* It allows resizing, combining, and managing storage more efficiently than traditional partitions.
* Useful for managing large or dynamic storage needs.

### 2. Key Concepts in LVM

#### a) Physical Volume (PV)

* A physical storage device (like a disk or partition) initialized for use by LVM.
* Command: `pvcreate /dev/sdb`

#### b) Volume Group (VG)

* A pool of storage created by combining one or more physical volumes.
* Command: `vgcreate my_vg /dev/sdb`

#### c) Logical Volume (LV)

* A "virtual partition" created from a volume group.
* Command: `lvcreate -L 10G -n my_lv my_vg`

### 3. Visual Hierarchy

```
[ Physical Volume(s) ]
           ↓
   [ Volume Group ]
           ↓
[ Logical Volume(s) ]
```

### 4. Common Commands

```bash
# Create Physical Volume
pvcreate /dev/sdb

# Create Volume Group
vgcreate my_vg /dev/sdb

# Create Logical Volume (e.g., 10GB)
lvcreate -L 10G -n my_lv my_vg

# Format and Mount
mkfs.ext4 /dev/my_vg/my_lv
mkdir /mnt/mydata
mount /dev/my_vg/my_lv /mnt/mydata

# Display Info
pvs       # Physical Volumes
vgs       # Volume Groups
lvs       # Logical Volumes
```

### 5. Why Use LVM?

* Resize volumes without downtime.
* Combine multiple disks into one large volume.
* Snapshots for backup.
* Easier management for dynamic environments.

### 6. Complete Flow Example

**Scenario:** 3 EBS volumes -> 3GB, 5GB, 2GB. Requirement: 10GB total, 2GB each partition.

#### Physical Volumes (PVs)

```bash
pvcreate /dev/xvdf /dev/xvdg /dev/xvdh
```

#### Volume Group (VG)

```bash
vgcreate vg_data /dev/xvdf /dev/xvdg /dev/xvdh
# Total size = 10 GB
```

#### Logical Volumes (LVs)

```bash
lvcreate -L 2G -n lv_app1 vg_data
lvcreate -L 2G -n lv_app2 vg_data
lvcreate -L 2G -n lv_app3 vg_data
lvcreate -L 2G -n lv_app4 vg_data
lvcreate -L 2G -n lv_app5 vg_data
```

**Format and Mount Example:**

```bash
mkfs.ext4 /dev/vg_data/lv_app1
mount /dev/vg_data/lv_app1 /mnt/app1
```

**Extending VG and LV:**

```bash
vgextend vg_data /dev/xvdi
lvextend -L +2G /dev/vg_data/lv_app1
resize2fs /dev/vg_data/lv_app1
```

## Practice Shell Script Questions

1. Check available free memory and alert if below threshold.
2. Automate creation of a new user with specific permissions and home directory.
3. Find all large files >1GB in a directory and move them to another directory.
4. Automatically update all installed packages and reboot if needed.
5. Count number of lines in all `.log` files in a directory.
6. Check for the presence of specific software (Docker, Git) and install if missing.