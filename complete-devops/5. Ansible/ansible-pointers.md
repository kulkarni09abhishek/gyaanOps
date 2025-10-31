# Ansible Notes

## Idempotency & Declarative Automation
Idempotency means that executing the same Ansible playbook multiple times will always result in the same final state, without causing additional changes once the target condition is met.

Key characteristics:
- If a package is already installed, Ansible will not reinstall it.
- If a file already exists with correct content/permissions, Ansible will not update it.
- Ensures safe re-runs of automation tasks.

Ansible follows a declarative approach: you describe **what the final state should be**, instead of specifying the procedural steps to reach that state.
Example:
```yaml
- name: Ensure nginx is installed
  apt:
    name: nginx
    state: present
```

## Infrastructure as Code (IaC) vs Configuration Management vs Orchestration

| Feature | Terraform | Ansible | Packer | Chef/Puppet |
|--------|-----------|---------|--------|-------------|
| Primary Usage | Provision infrastructure | Configure systems and deploy apps | Build machine images | Continuous config enforcement |
| Workflow Stage | Day 0 | Day 1 → Day N | Pre-Day 0 | Ongoing |
| Execution | Declarative | Declarative + Imperative tasks | Declarative | Declarative |
| Agent Required | No | No | No | Yes |
| Infra/Config Focus | Infra | Config | Pre-baked images | Config |


## Security-First Automation Practices in Ansible
Security-first automation ensures that automation workflows, credentials, and configuration changes follow security best practices and avoid exposing sensitive information.

---

### 1️⃣ Protect Sensitive Data (Secrets Management)
- Avoid hardcoding passwords, tokens, certificates in playbooks
- Use **Ansible Vault** to encrypt:
  - Variables
  - Inventory files
  - Credentials
- Integrate with enterprise secret managers:
  - HashiCorp Vault
  - AWS Secrets Manager
  - Azure Key Vault

Example: Encrypting a secrets file
```bash
ansible-vault encrypt secrets.yml
```

### 2️⃣ Least Privilege Access
- Use minimal credentials for tasks
- Avoid running everything with root
- Prefer scoped SSH keys or fine-grained IAM permissions

Limit privilege escalation using:

```yaml
become: true
become_user: nginx
```


### Auditability & Logging
- Enable verbose logs and store execution history
- Use --check mode for dry-run auditing

```bash
ansible-playbook site.yml --check --diff
```
Track change reports for compliance audits


## GitOps Patterns & Branching Workflows for Ansible

GitOps applies Git as the **single source of truth** for infrastructure and configuration changes.  
All automation actions are triggered from version-controlled updates.

---

### GitOps Principles Applied to Ansible

| Principle | How Ansible Fits |
|----------|-----------------|
| Everything in Git | Playbooks, inventories, vars, CI workflows |
| Automated delivery | CI/CD pipeline runs Ansible after approved commits |
| Immutable changes | No manual edits on servers; all changes flow from Git |
| Review & approvals | PR/MR required before executing automation |
| Observability | Automation logs and state changes fully traceable |

---

### Recommended Git Repositories Structure
ansible-repo/
├── inventories/
│ ├── dev/
│ ├── qa/
│ └── prod/
├── roles/
├── playbooks/
└── group_vars/host_vars


## Environment-Specific Overrides

Patterns:
- `group_vars`
- `host_vars`
- Separate inventory per environment
- Avoid branching differences for each environment (anti-pattern)

Example:
roles/
group_vars/
dev.yml
prod.yml
inventory/
dev
prod

Config changes live in **variables**, not code branches.


## Interview Notes

- Git stores desired state → Ansible enforces it
- No one logs into servers to fix things → changes only via Git merges
- Inventories per environment with same playbooks improves reusability
- Automated testing (`lint`, `--check`, CI jobs) is key in GitOps adoption



## Ansible vs Ansible-Playbook vs Ansible-Doc vs Ansible-Console

Ansible provides multiple command-line utilities to perform different automation tasks.  
Each serves a distinct purpose — from running ad-hoc commands to exploring modules and debugging playbooks.

---

| Command            | Purpose                       | Common Use                   | Example                     |
| ------------------ | ----------------------------- | ---------------------------- | --------------------------- |
| `ansible`          | Run ad-hoc one-liner commands | Quick checks or fixes        | `ansible all -m ping`       |
| `ansible-playbook` | Execute YAML playbooks        | Full automation workflows    | `ansible-playbook site.yml` |
| `ansible-doc`      | View module documentation     | Reference & learning         | `ansible-doc yum`           |
| `ansible-console`  | Interactive Ansible shell     | Live debugging & exploration | `ansible-console web`       |



## ansible.cfg Structure & Tuning (forks, retries, timeout, etc.)

`ansible.cfg` is the main configuration file that controls how Ansible behaves at runtime.  
It defines defaults for inventory, connection, privilege escalation, logging, performance tuning, and more.

---

### 📁 Location & Precedence

Ansible searches for `ansible.cfg` in the following order (highest to lowest priority):

1. `ANSIBLE_CONFIG` (environment variable)
2. `./ansible.cfg` (in current working directory)
3. `~/.ansible.cfg` (in user’s home directory)
4. `/etc/ansible/ansible.cfg` (default location)

The **first found** file in this order is used.

---

### ⚙️ Typical `ansible.cfg` Structure

```ini
[defaults]
inventory = ./inventory
remote_user = ec2-user
host_key_checking = False
forks = 10
timeout = 30
retry_files_enabled = True
retry_files_save_path = ./retries
log_path = ./ansible.log
roles_path = ./roles
deprecation_warnings = False

[privilege_escalation]
become = True
become_method = sudo
become_user = root
become_ask_pass = False

[ssh_connection]
pipelining = True
ssh_args = -o ControlMaster=auto -o ControlPersist=60s
retries = 3
```


- forks => Defines how many hosts Ansible can manage in parallel.
- timeout => Controls how long (in seconds) Ansible waits for a host connection before failing.
- retries => Defines how many times Ansible retries failed connections or commands.
- host_key_checking => Disables SSH host key verification to avoid prompts during automation (use with caution).

### View configurations 
- ansible-config list   => # Lists all configurations
- ansible-config view   => # Shows the current config file
- ansible config dump   => # Shows the current settings


### 🧠 Interview Notes
- forks → parallelism (performance tuning)
- timeout → SSH wait duration
- retries → resilience against transient network issues
- pipelining → fewer SSH operations = faster playbooks
- ansible.cfg can override command-line defaults
- Location precedence: env var > local > user > system



# 🧭 Ansible Inventory 
The **Ansible inventory** defines *which systems (hosts)* Ansible will manage, and *how* it will connect to them.  
You can think of it as a **source of truth** for your infrastructure — containing hostnames, IPs, groups, and associated variables.

---

## 🧱 1. Types of Inventories

### 🔹 Static Inventory
Defined manually using files (`.ini` or `.yml`).  
Good for small setups or local testing.

**Example:**
```ini
[web]
web1 ansible_host=192.168.10.11
web2 ansible_host=192.168.10.12

[db]
db1 ansible_host=192.168.10.21
```

Or YAML format:

```yaml
all:
  children:
    web:
      hosts:
        web1:
          ansible_host: 192.168.10.11
        web2:
          ansible_host: 192.168.10.12
    db:
      hosts:
        db1:
          ansible_host: 192.168.10.21
```

🔹 Dynamic Inventory
Generated automatically by scripts or plugins for cloud environments (AWS, Azure, GCP, etc.) or container orchestrators (Kubernetes, OpenShift).

Examples:
- AWS → aws_ec2 plugin
- GCP → gcp_compute plugin
- Kubernetes → k8s plugin

Command Example:

```bash
ansible-inventory -i aws_ec2.yml --graph
```

Dynamic inventory is ideal for:
- Autoscaling environments
- Cloud-native deployments
- Large fleets with changing host IPs

🧩 2. Inventory Structure
Each inventory defines:

Hosts — Systems to manage
Groups — Logical collections of hosts
Variables — Host or group-specific configuration
Children Groups — Nested group hierarchy

Example:

```yaml
all:
  vars:
    ansible_user: ubuntu
  children:
    app_servers:
      hosts:
        app1:
          ansible_host: 10.0.0.11
        app2:
          ansible_host: 10.0.0.12
      vars:
        app_port: 8080
    db_servers:
      hosts:
        db1:
          ansible_host: 10.0.0.21
      vars:
        db_engine: mysql
```

## 🧠 3. How Ansible Finds and Uses Inventory
You can specify the inventory using:

```bash
ansible-playbook -i inventories/dev/inventory.yml playbook.yml
```

Or set a default in ansible.cfg:

```ini
[defaults]
inventory = inventories/dev/inventory.yml
```

To list all hosts:

```bash
ansible-inventory -i inventories/dev/inventory.yml --list
To visualize group hierarchy:
```

```bash
ansible-inventory -i inventories/dev/inventory.yml --graph
```

⚙️ 4. Variable Hierarchy and Sources
| Source             | Description                              | Example                                       |
| ------------------ | ---------------------------------------- | --------------------------------------------- |
| **group_vars/**    | Variables shared by all hosts in a group | `group_vars/web.yml`                          |
| **host_vars/**     | Variables specific to a host             | `host_vars/web1.yml`                          |
| **Inventory file** | Inline variable definition               | `web1 ansible_host=192.168.10.11 app_env=dev` |
| **Playbook vars**  | Declared inside playbook                 | `vars:` section                               |
| **Extra vars**     | Passed at runtime                        | `-e "key=value"`                              |

🧩 Precedence:
extra vars > playbook vars > host_vars > group_vars > inventory vars > defaults

🧮 5. Special Inventory Parameters
| Parameter                      | Description                               | Example                     |
| ------------------------------ | ----------------------------------------- | --------------------------- |
| `ansible_host`                 | IP/hostname to connect to                 | `ansible_host=192.168.1.10` |
| `ansible_user`                 | SSH username                              | `ansible_user=ubuntu`       |
| `ansible_port`                 | SSH port                                  | `ansible_port=2222`         |
| `ansible_connection`           | Connection type (ssh, local, winrm, etc.) | `ansible_connection=ssh`    |
| `ansible_ssh_private_key_file` | SSH private key                           | `~/.ssh/id_rsa`             |
| `ansible_become`               | Enable privilege escalation               | `yes`                       |


🧩 6. Group Hierarchy (Nested Groups)
You can nest groups using children:

```yaml
all:
  children:
    backend:
      children:
        db:
          hosts:
            db1:
            db2:
        cache:
          hosts:
            redis1:
            redis2:
```

This allows reusable roles:
backend → applies to all database & cache nodes
db → only database-related tasks
cache → Redis-related tasks

🧩 7. Environment-Based Directory Layout (Best Practice)
```css
inventories/
├── dev/
│   ├── inventory.yml
│   ├── group_vars/
│   │   ├── all.yml
│   │   ├── web.yml
│   │   └── db.yml
│   └── host_vars/
│       ├── web1.yml
│       └── db1.yml
├── prod/
│   ├── inventory.yml
│   ├── group_vars/
│   └── host_vars/
```

You can then run:
```bash
ansible-playbook -i inventories/dev/inventory.yml site.yml
ansible-playbook -i inventories/prod/inventory.yml site.yml
```

🚀 8. Dynamic Inventory Example (AWS EC2)
aws_ec2.yml:

```yaml
plugin: aws_ec2
regions:
  - ap-south-1
filters:
  tag:Environment: dev
keyed_groups:
  - key: tags['Role']
    prefix: role
compose:
  ansible_host: public_ip_address
Run:
```

```bash
ansible-inventory -i aws_ec2.yml --graph
```

🧩 9. Combining Multiple Inventories
You can specify multiple inventory sources:

```bash
ansible-playbook -i inventories/dev/inventory.yml -i inventories/shared/inventory.yml playbook.yml
```

Ansible merges them automatically, allowing shared or environment-specific definitions.

🧰 10. Useful Inventory Commands
Command	Purpose
ansible-inventory --list	Dumps all inventory variables
ansible-inventory --graph	Shows host/group tree
ansible-inventory --yaml	Outputs in YAML format
ansible all -m ping -i inventory.yml	Checks connectivity to all hosts


# 🧩 Ansible Variables

Variables are **key-value pairs** that allow Ansible playbooks to be **dynamic and reusable**.  
They let you customize tasks for different hosts, environments, and conditions without hardcoding values.

---

## 🧱 1. Variable Types

Ansible supports multiple **variable sources** and types.  
They can exist in inventories, playbooks, roles, facts, or even be passed at runtime.

| Type | Description | Example |
|------|--------------|----------|
| **Inventory variables** | Defined in inventory files (`.ini`/`.yml`) | `web1 ansible_host=192.168.1.10 app_env=dev` |
| **Group variables** | Apply to all hosts in a group | `group_vars/web.yml` |
| **Host variables** | Specific to one host | `host_vars/web1.yml` |
| **Playbook variables** | Declared under `vars:` or `vars_files:` | `vars: { port: 8080 }` |
| **Role variables** | Declared in `roles/role_name/vars/main.yml` |
| **Registered variables** | Created dynamically from task results |
| **Facts** | Auto-discovered host data (`ansible_facts`) |
| **Extra variables** | Passed at runtime using `-e` flag |

Example usage:
```yaml
- name: Demo
  hosts: all
  vars:
    app_name: myapp
  tasks:
    - debug:
        msg: "Deploying {{ app_name }}"
```

## 🧠 2. Registering Variables & Precedence
🔹 Registering Variables
You can capture the output of a task into a variable using register:.

```yaml
- name: Check if a file exists
  stat:
    path: /etc/hosts
  register: file_info

- name: Display registered variable
  debug:
    msg: "File size is {{ file_info.stat.size }} bytes"
```

🧩 Registered variables are stored temporarily (in memory) and scoped to the host that ran the task.

🔹 Variable Precedence (Highest to Lowest)
Ansible merges variables from multiple sources, but the last definition wins unless scoped tightly.

| Priority | Variable Source        | Example                                   |
| -------- | ---------------------- | ------------------------------------------|
| 1️⃣      | **Extra vars** (`-e`)  | `ansible-playbook play.yml -e "port=9000"` |
| 2️⃣      | **Task vars** (inline) | Inside a specific task                     |
| 3️⃣      | **Block vars**         | Inside `block:` section                    |
| 4️⃣      | **Role vars**          | `roles/role_name/vars/main.yml`            |
| 5️⃣      | **Play vars**          | Declared under `vars:`                     |
| 6️⃣      | **Host vars**          | `host_vars/hostname.yml`                   |
| 7️⃣      | **Group vars**         | `group_vars/web.yml`                       |
| 8️⃣      | **Inventory vars**     | Defined in `inventory.yml`                 |
| 9️⃣      | **Facts**              | Gathered automatically                     |
| 🔟      |  **Role defaults**     |  `roles/role_name/defaults/main.yml`       |


## 🌐 3. Variable Scoping
Scope defines where a variable is valid and accessible.
| Scope Type       | Description                                                  | Example                        |
| ---------------- | ------------------------------------------------------------ | ------------------------------ |
| **Global scope** | Defined via command line, config, or inventory               | `ansible_user`, `ansible_host` |
| **Play scope**   | Declared in a playbook and applies to all hosts in that play | `vars:` in a playbook          |
| **Host scope**   | Applies to a single host (e.g., `host_vars`)                 | Host-specific data             |
| **Task scope**   | Declared in a task and lives only for that task              | `vars:` inside a task          |

```yaml
- name: Demonstrate scoping
  hosts: all
  vars:
    global_var: "I am play-level"
  tasks:
    - name: Local var
      vars:
        local_var: "I am task-level"
      debug:
        msg: "{{ global_var }} and {{ local_var }}"
```


## 🧙‍♂️ 4. Magic Variables
Ansible provides built-in special variables that give context about the play, host, or runtime environment.| Magic Variable       | Description                                       |
| -------------------- | ------------------------------------------------- |
| `inventory_hostname` | The name of the current host (from inventory)     |
| `ansible_host`       | IP or hostname used to connect                    |
| `group_names`        | List of all groups the host belongs to            |
| `groups`             | Dictionary of all groups and their hosts          |
| `play_hosts`         | Hosts targeted by the current play                |
| `hostvars`           | Dictionary of all variables per host              |
| `inventory_dir`      | Directory containing the current inventory file   |
| `inventory_file`     | The path of the inventory file in use             |
| `role_path`          | Path of the current role                          |
| `ansible_play_batch` | Hosts in the current batch (when using `serial:`) |

```yaml
- name: Magic variable demo
  hosts: all
  tasks:
    - debug:
        msg:
          - "Current host: {{ inventory_hostname }}"
          - "Host IP: {{ ansible_host }}"
          - "Groups: {{ group_names }}"
          - "App servers in same inventory: {{ groups['web'] }}"
```

## ⚡ 5. Gathering Facts
Facts are system information automatically collected from hosts when a play starts using **setup module**.
They include OS, IP, CPU, memory, network interfaces, etc.

🔹 Example:
```yaml
- name: Display facts
  hosts: all
  tasks:
    - debug:
        msg:
          - "OS: {{ ansible_distribution }}"
          - "Version: {{ ansible_distribution_version }}"
          - "IP: {{ ansible_default_ipv4.address }}"
```

🔹 To disable fact gathering:
```yaml
- hosts: all
  gather_facts: no
```

🔹 To gather facts manually:
```yaml
- setup:
    gather_subset:
      - network
      - hardware
```

🔹 To filter specific facts:
```bash
ansible all -m setup -a 'filter=ansible_distribution*'
```

## 🧰 6. Variable Debugging Tips
| Command                                      | Purpose                            |
| -------------------------------------------- | ---------------------------------- |
| `ansible-inventory --host <hostname>`        | Show host variables                |
| `ansible -m debug -a "var=hostvars['web1']"` | Print all variables for a host     |
| `ansible -m setup`                           | Show gathered facts                |
| `ansible-playbook play.yml -e "debug=true"`  | Override or inject vars at runtime |

Keep variables modular: use group_vars for shared configs, host_vars for specifics, and register + facts for runtime discovery.


# 🧠 Ansible Facts & Custom Facts

Facts are **pieces of information automatically gathered** from managed nodes about their system state — OS, hardware, network, users, etc.  
They make Ansible **context-aware** so your playbooks can adapt dynamically.

---

## ⚙️ 1. What Are Facts?

When `gather_facts: yes` (default), Ansible runs the **`setup` module** on each host at the start of a play.  
This collects details like:

- OS type and version  
- IP addresses and network interfaces  
- Memory, CPU, and disk details  
- Hostname, users, architecture, etc.  

Facts are stored in a variable dictionary called `ansible_facts`.

Example:
```yaml
- name: Display some facts
  hosts: all
  tasks:
    - debug:
        msg:
          - "OS: {{ ansible_facts['distribution'] }}"
          - "Version: {{ ansible_facts['distribution_version'] }}"
          - "Default IPv4: {{ ansible_facts['default_ipv4']['address'] }}"
```

🧩 2. Using the setup Module
You can collect facts manually or filter what to gather.

🔹 Gather all facts (default)
```bash
ansible all -m setup
```

🔹 Filter specific facts
```bash
ansible all -m setup -a 'filter=ansible_distribution*'
```

🔹 Limit what to gather (for speed)
```yaml
- setup:
    gather_subset:
      - network
      - hardware
```

🔹 Exclude certain categories
```yaml
- setup:
    gather_subset:
      - '!all'
      - 'min'
```

🧠 Tip: !all + min gives you just the basics — useful for performance on large inventories.

## 🧱 3. Custom Facts (Local Facts)
You can define your own facts on managed hosts, stored as files under:
/etc/ansible/facts.d/

Custom facts can be INI, JSON, or YAML format.

🧩 Example 1 — INI-based Custom Fact

Create /etc/ansible/facts.d/app.ini on the target host:
```ini
[app]
role=web
environment=dev
version=1.2.0
```

Now you can use them in your playbook:
```yaml
- name: Show custom facts
  hosts: all
  tasks:
    - debug:
        msg:
          - "App role: {{ ansible_local.app.role }}"
          - "App version: {{ ansible_local.app.version }}"
```

🧩 Example 2 — JSON-based Custom Fact
/etc/ansible/facts.d/app.json:
```json
{
  "role": "database",
  "environment": "prod",
  "version": "5.7"
}
```

Access in playbook:
```yaml
- debug:
    msg: "DB role: {{ ansible_local.app.role }} | Version: {{ ansible_local.app.version }}"
```
🧩 Example 3 — Deploying Custom Facts via Playbook

You can deploy fact files dynamically using Ansible itself:
```yaml
- name: Deploy custom facts
  hosts: all
  tasks:
    - name: Ensure facts directory exists
      file:
        path: /etc/ansible/facts.d
        state: directory
        mode: '0755'

    - name: Add custom fact file
      copy:
        dest: /etc/ansible/facts.d/app.json
        content: |
          {
            "environment": "staging",
            "app_owner": "platform-team",
            "build_id": "2025.10.30"
          }

    - name: Refresh facts
      setup:
        filter: ansible_local
```

🔍 4. Where Facts Live (Caching)
By default, facts are gathered at runtime and discarded.
But you can cache facts between playbook runs for faster performance or offline use.

In ansible.cfg:
```ini
[defaults]
gathering = smart
fact_caching = jsonfile
fact_caching_connection = /tmp/ansible_facts_cache
fact_caching_timeout = 7200
```

This stores facts in /tmp/ansible_facts_cache for 2 hours.


🧠 5. Fact Filtering & Debugging
| Command                                                | Description                                 |
| ------------------------------------------------------ | ------------------------------------------- |
| `ansible all -m setup`                                 | Gather and print all facts                  |
| `ansible all -m setup -a 'filter=ansible_processor*'`  | Filter facts by name                        |
| `ansible localhost -m setup -a 'filter=ansible_local'` | Display only custom facts                   |
| `ansible-inventory --host <hostname>`                  | Shows merged variables and facts for a host |


⚡ 6. Combining Facts with Conditionals
You can use facts in when conditions to build intelligent logic.
```yaml
- name: Install Nginx only on Debian-based systems
  hosts: all
  tasks:
    - name: Install Nginx
      apt:
        name: nginx
        state: present
      when: ansible_facts['os_family'] == "Debian"
```

Or use custom facts:
```yaml
- name: Run app setup for production only
  hosts: all
  tasks:
    - debug:
        msg: "Running setup for prod"
      when: ansible_local.app.environment == "prod"
```

🧰 7. Best Practices for Facts
✅ Enable smart fact gathering to avoid re-collecting facts unnecessarily
✅ Cache facts in CI/CD environments for speed
✅ Use ansible_local for app or environment metadata
✅ Filter facts when debugging (they can be very verbose)
✅ Never overwrite ansible_facts — store your own data in separate vars


# 🧩 Ansible Playbooks — Deep Dive
## 📘 What is an Ansible Playbook?
An Ansible Playbook is a YAML file that defines what tasks to run, on which hosts, and in what order.
A playbook = list of plays.
A play = mapping between hosts and tasks.
It’s declarative, not imperative — you describe the desired end state, and Ansible makes it so.

Example: Basic Playbook
---
```yaml
- name: Install and start Nginx
  hosts: webservers
  become: yes
  tasks:
    - name: Install nginx
      apt:
        name: nginx
        state: present

    - name: Start nginx
      service:
        name: nginx
        state: started
```

✅ Verifying Playbooks
You can validate syntax before execution using:
```bash
ansible-playbook --syntax-check playbook.yml
```
Or, do a dry run (simulation mode):
```bash
ansible-playbook playbook.yml --check
```

Output:
Shows what would change but doesn’t actually modify anything.


## 🧹 Ansible Lint
ansible-lint:
 checks for best practices and style errors in your playbooks.

Example:
```bash
ansible-lint playbook.yml
```

It detects:
- Deprecated modules
- Missing name in tasks
- Unquoted variables
- Inefficient loops or conditionals

Helps keep your playbooks clean, standardized, and maintainable.

## 🧠 Conditionals in Ansible
Conditionals control task execution based on logic.
```yaml
- name: Install Apache only on Ubuntu
  apt:
    name: apache2
    state: present
  when: ansible_facts['os_family'] == "Debian"
```

🎯 Based on Variables
```yaml
- name: Start nginx only if enabled
  service:
    name: nginx
    state: started
  when: nginx_enabled | bool
```

You can also chain conditions:
```yaml
when:
  - ansible_facts['distribution'] == "Ubuntu"
  - nginx_enabled | bool
```

🔁 Loops in Ansible
Loops are used to repeat tasks for multiple items.

Basic Example
```yaml
- name: Create multiple users
  user:
    name: "{{ item }}"
    state: present
  loop:
    - alice
    - bob
    - charlie
```

Loop with Dictionaries
```yaml
- name: Create users with attributes
  user:
    name: "{{ item.name }}"
    groups: "{{ item.groups }}"
  loop:
    - { name: 'alice', groups: 'admin' }
    - { name: 'bob', groups: 'dev' }
```

Using with_items (older syntax)
```yaml
- name: Install packages
  yum:
    name: "{{ item }}"
    state: present
  with_items:
    - git
    - vim
    - curl
```

Nested Loops
```yaml
- name: Combine users and groups
  debug:
    msg: "User {{ item.0 }} -> Group {{ item.1 }}"
  with_nested:
    - [ 'alice', 'bob' ]
    - [ 'admin', 'dev' ]
```

🧩 Combining Loops and Conditionals
```yaml
- name: Install web tools only on Ubuntu
  apt:
    name: "{{ item }}"
    state: present
  loop:
    - nginx
    - apache2
  when: ansible_facts['distribution'] == "Ubuntu"
```

🧪 Debugging and Output
```yaml
- name: Print variable values
  debug:
    msg: "Nginx is {{ nginx_enabled }}"
```

Or show loop outputs:
```yaml
- name: Show user creation result
  debug:
    var: item
  loop: "{{ users }}"
```


# ⚙️ Ansible Modules
## 🧩 What is an Ansible Module?
An Ansible Module is a unit of work — the smallest piece of code that Ansible executes on your target machines.
You can think of a module as a function that performs a specific automation task like installing a package, copying a file, or managing users.

💡 Every task in a playbook calls one module.
Example:
```yaml
- name: Install Nginx
  apt:
    name: nginx
    state: present
```

Here, apt is the module being used.
🧠 How Modules Work
- Modules are executed on the target node (not the controller).
- Ansible sends the module as a temporary Python script to the remote host.
- It runs the script and returns JSON output.
- Then Ansible removes the temporary file — leaving no footprint.

## 🧱 Module Types
Ansible has hundreds of built-in modules, organized by function:
| Category      | Examples                                | Description                                   |
| ------------- | --------------------------------------- | --------------------------------------------- |
| **System**    | `user`, `group`, `service`, `cron`      | Manage users, groups, services, and cron jobs |
| **Files**     | `copy`, `file`, `template`, `unarchive` | Handle files and directories                  |
| **Packages**  | `apt`, `yum`, `dnf`, `pip`              | Install or remove packages                    |
| **Cloud**     | `ec2`, `gcp_compute`, `azure_rm`        | Manage cloud resources                        |
| **Network**   | `ios_config`, `nxos_command`            | Manage network devices                        |
| **Database**  | `mysql_db`, `postgresql_user`           | Configure databases                           |
| **Utilities** | `command`, `shell`, `debug`, `set_fact` | Execute commands and manage variables         |


💥 Command vs Shell Modules
| Module    | Description                                                        | Example                 |            |
| --------- | ------------------------------------------------------------------ | ----------------------- | ---------- |
| `command` | Executes a command directly (no shell features)                    | `command: ls -l /opt`   |            |
| `shell`   | Executes command through a shell (supports pipes, redirects, etc.) | `shell: cat /etc/passwd | grep root` |

⚠️ Use shell only when absolutely needed — it’s less secure.


## 🧰 Commonly Used Modules
🗂️ File and Directory
```yaml
- name: Ensure directory exists
  file:
    path: /opt/myapp
    state: directory
    mode: '0755'
```

📄 Copy Files
```yaml
- name: Copy app config
  copy:
    src: ./app.conf
    dest: /etc/myapp/app.conf
```

🧱 Template (Jinja2)
```yaml
- name: Generate config from template
  template:
    src: nginx.conf.j2
    dest: /etc/nginx/nginx.conf
```

👤 User Management
```yaml
- name: Create a new user
  user:
    name: devops
    groups: sudo
    state: present
```

💡 Package Installation
```yaml
- name: Install packages
  yum:
    name:
      - git
      - vim
    state: present
```

## 🧮 Custom Modules
You can create your own modules in Python or any scripting language that returns JSON.

Example structure:
```markdown
my-ansible-project/
├── playbook.yml
└── library/
    └── hello_module.py
```
**🧠 The library/ folder is special — Ansible automatically detects modules from here.**

```python
#!/usr/bin/python
from ansible.module_utils.basic import AnsibleModule

def main():
    module = AnsibleModule(
        argument_spec={'name': {'required': True, 'type': 'str'}}
    )
    name = module.params['name']
    module.exit_json(changed=False, msg=f"Hello {name}")

if __name__ == '__main__':
    main()
```
✅ Key Points:
- Every module must call AnsibleModule() from ansible.module_utils.basic.
- It must return JSON output using module.exit_json() (success) or module.fail_json() (failure).
- The module can handle check mode safely.

Save this as library/hello.py and use it in your playbook:
```yaml
- name: Custom module example
  hello:
    name: Abhishek
```

⚡ Module Return Values
Modules return JSON responses that Ansible uses to determine if something changed.
Example:
```json
{
  "changed": true,
  "msg": "Package nginx installed",
  "rc": 0
}
```

🔹 changed tells whether Ansible made any modification.
🔹 If no change was needed, Ansible reports “ok”.

---

# 🔁 Ansible Handlers — Deep Dive
🧩 What is a Handler?
A Handler in Ansible is a special kind of task that runs only when notified by another task.
They’re typically used for actions that should happen only when something changes — e.g., restarting a service after updating a config file.

💡 Think of handlers as “if-changed, then-do-this” automation logic.

## 🧠 Why Handlers?
Without handlers, tasks would restart services every time you run your playbook — even when nothing changed.
Handlers make playbooks:
- Idempotent (no redundant actions)
- Efficient (run only when needed)
- Predictable (clear trigger-action pattern)

## 🧱 Handler Structure
Handlers live under a special section called **handlers:** in a playbook.

Example:
```yaml
- name: Configure and restart Nginx
  hosts: webservers
  become: yes

  tasks:
    - name: Copy nginx config
      copy:
        src: nginx.conf
        dest: /etc/nginx/nginx.conf
      notify: Restart Nginx

  handlers:
    - name: Restart Nginx
      service:
        name: nginx
        state: restarted
```


## 🧯 Multiple Notifications
If multiple tasks notify the same handler, it still runs only once at the end of the play.
```yaml
tasks:
  - name: Update nginx.conf
    copy:
      src: nginx.conf
      dest: /etc/nginx/nginx.conf
    notify: Restart Nginx

  - name: Update site config
    template:
      src: site.conf.j2
      dest: /etc/nginx/conf.d/site.conf
    notify: Restart Nginx

handlers:
  - name: Restart Nginx
    service:
      name: nginx
      state: restarted
```

**Even if both tasks changed files, Ansible restarts Nginx only once after all tasks complete.**
🎯 Super efficient!

## ⚡ Multiple Handlers from a single task
You can also trigger multiple handlers from a single task:
```yaml
tasks:
  - name: Deploy new config
    copy:
      src: app.conf
      dest: /etc/myapp/app.conf
    notify:
      - Restart App
      - Send Notification

handlers:
  - name: Restart App
    service:
      name: myapp
      state: restarted

  - name: Send Notification
    debug:
      msg: "Application restarted successfully"
```

## 🧩 Handlers in Separate Files
In large projects, you can organize handlers into dedicated files for reusability.
```bash
roles/
  webserver/
    tasks/main.yml
    handlers/main.yml
```

Then define them like this:
```yaml
# roles/webserver/handlers/main.yml
- name: Restart Nginx
  service:
    name: nginx
    state: restarted
```

## ⏱️ Triggering Handlers Immediately
By default, handlers run after all tasks in a play are complete.
But sometimes you need an immediate restart (e.g., reload a service before continuing).

You can use:
```yaml
meta: flush_handlers
```

Example
```yaml
tasks:
  - name: Update config
    copy:
      src: nginx.conf
      dest: /etc/nginx/nginx.conf
    notify: Restart Nginx

  - meta: flush_handlers

  - name: Continue setup
    debug:
      msg: "Handlers have already run"
```

---

# 🚨 Error Handling in Ansible — Deep Dive
## 🧩 Why Error Handling Matters
By default, Ansible stops executing a playbook as soon as a task fails.
This behavior ensures safety — but sometimes, you may want:
- To ignore certain errors
- To retry tasks
- To continue execution
- Or even to handle errors gracefully (like try–catch)

Ansible gives us multiple tools for this!

## ⚙️ 1. Default Behavior — Stop on Error
By default:
```yaml
- name: This will stop the playbook on failure
  command: /bin/false
```
➡️ The playbook will fail immediately when /bin/false returns a non-zero exit code.


## 🙈 2. Ignoring Errors
If a task fails but you want Ansible to continue, use:
```yaml
ignore_errors: yes
```
Example
```yaml
- name: Try removing a file that may not exist
  file:
    path: /tmp/missing.txt
    state: absent
  ignore_errors: yes
```

🧠 Use this wisely — it hides errors, which can lead to debugging nightmares later.

## 🔁 3. Retrying Tasks Until Success
You can make a task retry multiple times until it succeeds using:
```yaml
retries: <number>
delay: <seconds>
until: <condition>
```
Example
```yaml
- name: Wait for webserver to start
  uri:
    url: http://localhost:8080
    status_code: 200
  register: result
  retries: 5
  delay: 10
  until: result.status == 200
```
🧩 This keeps retrying every 10 seconds until the condition passes (or 5 attempts fail).

## 🧱 4. Using Blocks — Try/Catch Style
Blocks allow you to group tasks together and handle their success or failure collectively.
Example
```yaml
- block:
    - name: Install nginx
      apt:
        name: nginx
        state: present

    - name: Start nginx
      service:
        name: nginx
        state: started

  rescue:
    - name: Rollback or alert
      debug:
        msg: "Something went wrong with Nginx installation"

  always:
    - name: Cleanup temp files
      file:
        path: /tmp/nginx.lock
        state: absent
```

🧠 Think of it like:
- block → try
- rescue → catch
- always → finally

## 💥 5. Failing Intentionally
Sometimes you want to fail a playbook on purpose when a condition is not met.
Use the fail module:
```yaml
- name: Ensure minimum free memory
  fail:
    msg: "Not enough memory available!"
  when: ansible_facts['memfree_mb'] < 512
```
✅ If the condition is false, task passes.
❌ If true, playbook fails with your custom message.

## ⚠️ 6. Warning Instead of Failing
You can print warnings without failing:
```yaml
- name: Warn user if disk usage is high
  debug:
    msg: "Warning: Disk usage above 90%"
  when: disk_usage > 90
```

Or use ansible.builtin.assert to fail or warn:
```yaml
- name: Check Python version
  assert:
    that:
      - ansible_facts['python_version'] is version('3.8', '>=')
    fail_msg: "Python 3.8 or higher required"
    success_msg: "Python version OK"
```

## 🧪 7. Handling Errors with Registered Variables
You can capture task output and decide based on it:
```yaml
- name: Run risky command
  command: /bin/false
  register: result
  ignore_errors: yes

- name: Print result if failed
  debug:
    msg: "Command failed with msg: {{ result.msg }}"
  when: result.failed
```
🧠 Useful when you want to analyze the error before deciding what to do.

## 🚧 8. Error Handling in Loops
When using loops, a single failure stops the whole loop.
You can bypass that with:
```yaml
- name: Continue loop even if one fails
  shell: "ping -c1 {{ item }}"
  loop:
    - google.com
    - fakewebsite.local
    - yahoo.com
  ignore_errors: yes
```

## 🧩 9. Controlling Failure Behavior per Host
If one host fails, by default Ansible skips that host.
But you can control this behavior using max_fail_percentage or any_errors_fatal.
Example:
```yaml
- hosts: webservers
  any_errors_fatal: true   # If one fails, stop all
  tasks:
    - name: Example task
      command: /bin/false
```

---

# 🧱 Ansible Roles
## 🧩 What is a Role?
A Role in Ansible is a standardized, reusable structure for organizing related automation logic — like tasks, handlers, templates, and variables — into a clean, modular unit.
Instead of one big messy playbook, roles break it down into organized components.

## 🗂️ Typical Role Directory Structure
When you create a role, it automatically follows a standard layout:
```css
my-ansible-project/
├── playbook.yml
└── roles/
    └── webserver/
        ├── tasks/
        │   └── main.yml
        ├── handlers/
        │   └── main.yml
        ├── templates/
        │   └── nginx.conf.j2
        ├── files/
        │   └── index.html
        ├── vars/
        │   └── main.yml
        ├── defaults/
        │   └── main.yml
        ├── meta/
        │   └── main.yml
        └── README.md
```

🔍 Role Directory Breakdown
| Directory      | Purpose                                          |
| -------------- | ------------------------------------------------ |
| **tasks/**     | Main automation logic (executed in order)        |
| **handlers/**  | Handlers triggered by `notify`                   |
| **templates/** | Jinja2 templates (`.j2` files)                   |
| **files/**     | Static files to copy to hosts                    |
| **vars/**      | Variables with high precedence                   |
| **defaults/**  | Variables with lowest precedence (safe defaults) |
| **meta/**      | Role metadata (dependencies, author, etc.)       |


##⚡ Creating a Role
You can create a role manually or use Ansible’s built-in generator:
```bash
ansible-galaxy init webserver
```
This creates the full folder structure automatically 🎉

🧩 Example Role — Webserver
🔹 roles/webserver/tasks/main.yml
```yaml
---
- name: Install Nginx
  apt:
    name: nginx
    state: present
  notify: Restart Nginx

- name: Deploy index.html
  copy:
    src: index.html
    dest: /var/www/html/index.html
```
🔹 roles/webserver/handlers/main.yml
```yaml
---
- name: Restart Nginx
  service:
    name: nginx
    state: restarted
```
🔹 roles/webserver/defaults/main.yml
---
```yaml
nginx_port: 80
```

## 🧾 Using a Role in a Playbook
playbook.yml
---
```yaml
- name: Deploy Web Application
  hosts: webservers
  become: yes

  roles:
    - webserver
```
That’s it — all the logic inside the webserver role runs automatically in the right order:
Defaults →
Vars →
Tasks →
Handlers (if notified)

## 🧠 Role Dependencies
Sometimes one role depends on another.
You can define dependencies in meta/main.yml:
---
```yaml
dependencies:
  - role: common
  - role: firewall
```
When webserver runs, it automatically triggers common and firewall first.

## 🧩 Passing Variables to Roles
You can pass variables when calling the role:
```yaml
roles:
  - role: webserver
    vars:
      nginx_port: 8080
```
Or from inventory/group_vars/host_vars.

## 🔄 Including Roles Dynamically
Roles can also be included conditionally within tasks:
```yaml
- name: Include webserver role only for Ubuntu
  include_role:
    name: webserver
  when: ansible_facts['os_family'] == "Debian"
```

## 🧰 Roles vs Playbooks — When to Use
| Use Case                   | Playbook | Role |
| -------------------------- | -------- | ---- |
| Small ad-hoc automation    | ✅        | ❌    |
| Large-scale infrastructure | ❌        | ✅    |
| Reusability                | ❌        | ✅    |
| Multiple teams working     | ❌        | ✅    |
| One-off script             | ✅        | ❌    |

## 💡 Pro Tips
- Keep role names simple and meaningful (e.g., nginx, postgres, security-hardening).
- Store common defaults in defaults/main.yml and environment overrides in vars/main.yml.
- Combine roles + tags for selective execution:
```bash
ansible-playbook playbook.yml --tags "webserver"
```
- Reuse roles from Ansible Galaxy:
```bash
ansible-galaxy install geerlingguy.nginx
```


# 🌌 Ansible Galaxy & Collections — Deep Dive
## 🚀 What Is Ansible Galaxy?
Ansible Galaxy is the official hub for sharing and downloading Ansible roles and collections.
It’s like Docker Hub, but for Ansible content — a public registry where you can:
🔹 Find and use pre-built roles or collections
🔹 Publish your own automation for reuse
🔹 Version control your automation packages

Website: https://galaxy.ansible.com

## 🧱 What Are Collections?
A Collection is a distribution format that bundles:
- Roles
- Modules
- Plugins
- Playbooks
- Documentation

All in a single package that can be shared, versioned, and imported easily.
💡 Think of a collection as a “containerized” set of automation resources — portable, namespaced, and dependency-aware.

## 🧩 Galaxy vs Collections
| Feature                  | **Ansible Galaxy Role** | **Ansible Collection**                     |
| ------------------------ | ----------------------- | ------------------------------------------ |
| **Purpose**              | Share a single role     | Share multiple roles, modules, and plugins |
| **Structure**            | Simple role directory   | Full namespace + roles + modules + plugins |
| **Namespace**            | Flat (no namespace)     | Hierarchical (`namespace.collection.role`) |
| **Example**              | `geerlingguy.nginx`     | `community.general`                        |
| **Distribution**         | `.tar.gz` per role      | `.tar.gz` per collection                   |
| **Versioning**           | Role-level              | Collection-level                           |
| **Modern Best Practice** | ❌ Legacy style          | ✅ Recommended                              |

🧭 Typical Collection Structure
Here’s how a collection is laid out:
```css
ansible_collections/
└── myorg/
    └── webops/
        ├── README.md
        ├── galaxy.yml
        ├── roles/
        │   ├── nginx/
        │   └── apache/
        ├── plugins/
        │   ├── modules/
        │   │   └── my_module.py
        │   └── filter/
        ├── playbooks/
        │   └── site.yml
        └── docs/
```

## ⚙️ Creating a Collection
Use the ansible-galaxy CLI tool to scaffold it:
```bash
ansible-galaxy collection init myorg.webops
```
This creates the standard structure for you.

Now you can add:
- Roles → roles/
- Custom modules → plugins/modules/
- Playbooks → playbooks/
- Docs → README.md

🧾 The galaxy.yml File
This is the metadata file for your collection (like package.json or pom.xml).
Example:
```yaml
namespace: myorg
name: webops
version: 1.0.0
readme: README.md
authors:
  - Abhishek Kulkarni
description: Collection to manage web servers
license: MIT
dependencies:
  "ansible.builtin": "*"
  "community.general": ">=5.0.0"
repository: https://github.com/myorg/ansible-webops
```

## 📦 Building and Installing a Collection
🔹 Build your collection tarball:
```bash
ansible-galaxy collection build
```
This creates:
myorg-webops-1.0.0.tar.gz

🔹 Install it locally:
```bash
ansible-galaxy collection install myorg-webops-1.0.0.tar.gz
```
or from Galaxy:
```bash
ansible-galaxy collection install myorg.webops
```

🔹 Install from requirements.yml:
```yaml
collections:
  - name: myorg.webops
    version: 1.0.0
  - name: community.general
    version: 9.0.0
```
Then:
```bash
ansible-galaxy collection install -r requirements.yml
```

## 🔍 Using a Collection in a Playbook
```yaml
---
- name: Deploy Web Server
  hosts: webservers
  roles:
    - role: myorg.webops.nginx
```
Or call a module from a collection:
```yaml
- name: Create user using community module
  community.general.user:
    name: testuser
    state: present
```

## 🌍 Popular Collections on Galaxy
| Collection          | Description                                               |
| ------------------- | --------------------------------------------------------- |
| `community.general` | 400+ general-purpose modules (users, files, system tools) |
| `ansible.posix`     | Linux/Unix management                                     |
| `amazon.aws`        | AWS resources (EC2, S3, IAM, etc.)                        |
| `kubernetes.core`   | Kubernetes objects management                             |
| `community.docker`  | Docker management modules                                 |
| `ansible.utils`     | Helper plugins and utilities                              |


You can also host collections privately using:
- Artifactory
- GitLab Package Registry
- Galaxy NG / Automation Hub (Red Hat)

Example install from Git:
```bash
ansible-galaxy collection install git+https://github.com/myorg/ansible-webops.git
```

## 🧠 Ansible Galaxy CLI Cheatsheet
| Command                                               | Purpose                          |
| ----------------------------------------------------- | -------------------------------- |
| `ansible-galaxy search nginx`                         | Search for roles/collections     |
| `ansible-galaxy info geerlingguy.nginx`               | View role info                   |
| `ansible-galaxy role install geerlingguy.nginx`       | Install a role                   |
| `ansible-galaxy collection install community.general` | Install a collection             |
| `ansible-galaxy list`                                 | Show installed roles/collections |
| `ansible-galaxy collection build`                     | Package a collection             |
| `ansible-galaxy collection init <namespace.name>`     | Initialize a new collection      |

## 💡 Best Practices
✅ Use collections for modern Ansible code — roles are still valid but collections are the scalable format.
✅ Version-lock dependencies in requirements.yml.
✅ Prefix your namespaces to avoid naming conflicts (myorg.devops, acme.security).
✅ Keep collections modular and self-contained — don’t mix unrelated logic.
✅ Use ansible-lint and CI checks before publishing.



# 🏷️ Ansible Tags & Task Control — Deep Dive
## 🎯 What Are Tags?
Tags let you run specific parts of a playbook instead of executing everything.
They act like labels attached to tasks, roles, or plays — super useful for selective execution, debugging, or partial deployments.

## 🧱 Defining Tags
You can assign one or more tags to a task, role, or play.
```yaml
---
- name: Setup Web Server
  hosts: webservers
  become: yes

  tasks:
    - name: Install Nginx
      yum:
        name: nginx
        state: present
      tags:
        - install
        - web

    - name: Configure Nginx
      template:
        src: nginx.conf.j2
        dest: /etc/nginx/nginx.conf
      tags: config

    - name: Start Nginx
      service:
        name: nginx
        state: started
      tags: start
```

🧩 Running Tagged Tasks
| Command                                             | Description                     |
| --------------------------------------------------- | ------------------------------- |
| `ansible-playbook site.yml --tags install`          | Run only tasks tagged `install` |
| `ansible-playbook site.yml --tags "install,config"` | Run both tags                   |
| `ansible-playbook site.yml --skip-tags start`       | Skip tasks tagged `start`       |

💡 Tip: You can combine --tags and --skip-tags for fine control.

## 🧰 Default & Always Tags
always → Task runs every time, even if you use --tags filtering.
never → Task runs only when explicitly tagged via --tags never.
```yaml
- name: Check environment
  debug:
    msg: "This task always runs"
  tags: always
```

🔁 Tagging Roles and Includes
You can also apply tags to:
Entire roles
Imported or included task files
Example
```yaml
- hosts: web
  roles:
    - role: nginx
      tags: webserver
```
Example with includes:
```yaml
- include_tasks: db.yml
  tags: database
```
Now running:
```bash
ansible-playbook site.yml --tags database
```
executes only the tasks from db.yml.

---

# 🔐 Ansible Vault — Encrypting Secrets in Automation
## 🧩 What Is Ansible Vault?
Ansible Vault allows you to encrypt sensitive data — such as passwords, SSH keys, API tokens, or entire files — so you can safely commit them to version control.
💡 Think of Vault as built-in encryption for your playbooks and variables — keeping your automation secure and auditable.

## 🛠️ Why Use Vault?
- Protect sensitive credentials (DB passwords, API keys, tokens)
- Safely store encrypted files in Git
- Ensure team access control (via vault passwords or keys)
- Avoid hard-coded secrets in playbooks

## 🔑 Common Use Cases
| Scenario                     | Vault Usage                          |
| ---------------------------- | ------------------------------------ |
| Encrypt database credentials | `vault.yml` with encrypted variables |
| Protect private keys         | Encrypt `.pem` or `.key` files       |
| Secure cloud credentials     | Store AWS or GCP keys encrypted      |
| Encrypt entire playbook      | Prevent unauthorized task viewing    |

## ⚙️ Basic Commands
| Command                          | Description                     |
| -------------------------------- | ------------------------------- |
| `ansible-vault create file.yml`  | Create a new encrypted file     |
| `ansible-vault edit file.yml`    | Edit an encrypted file securely |
| `ansible-vault view file.yml`    | View encrypted file contents    |
| `ansible-vault encrypt file.yml` | Encrypt an existing file        |
| `ansible-vault decrypt file.yml` | Decrypt a file                  |
| `ansible-vault rekey file.yml`   | Change the encryption password  |

## 🧱 Creating an Encrypted File
```bash
ansible-vault create secrets.yml
```
You’ll be prompted for a password, then dropped into your default editor.
Example contents:
```yaml
db_user: admin
db_password: My$ecretPass123
```
Once saved, the file becomes encrypted:
```bash
$ANSIBLE_VAULT;1.1;AES256
616263646566...
```

## 🔒 Encrypt Existing Files
```bash
ansible-vault encrypt vars.yml
```
To decrypt:
```bash
ansible-vault decrypt vars.yml
```
Or rekey (change password):
```bash
ansible-vault rekey vars.yml
```
**Note: We need password to decrypt.**

## 🧠 Using Vault in Playbooks
You can include encrypted variables using vars_files:
```yaml
---
- name: Deploy App with Vault Secrets
  hosts: web
  vars_files:
    - secrets.yml

  tasks:
    - name: Print database user
      debug:
        msg: "Database user: {{ db_user }}"
```

Run it like this:
```bash
ansible-playbook site.yml --ask-vault-pass
```
It’ll prompt for the vault password before decrypting.

### 🪄 Automating Vault Access
Instead of entering the password interactively, store it in a vault password file:
```bash
echo "MyVaultPassword" > ~/.vault_pass.txt
chmod 600 ~/.vault_pass.txt
```
Then run:
```bash
ansible-playbook site.yml --vault-password-file ~/.vault_pass.txt
```
Or specify in ansible.cfg:
```bash
[defaults]
vault_password_file = ~/.vault_pass.txt
```
**⚠️ Always .gitignore your password file — never commit it to version control.**

## 🧩 Encrypting Individual Variables Inline
Instead of encrypting whole files, you can encrypt individual values using:
```bash
ansible-vault encrypt_string 'SuperSecret123' --name 'db_password'
```
Output:
```bash
db_password: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          333062656335663633343062353734326438653564623566333733383662...
```
Then paste that directly into your playbook or variable file.

## 🔐 Multi-Vault Environment Setup
In complex environments (e.g., dev/staging/prod), you can maintain multiple vaults with different passwords:
```yaml
vars_files:
  - secrets/dev.yml
  - secrets/prod.yml
```
Use different vault password files:
```bash
ansible-playbook site.yml --vault-id dev@~/.vault_dev.txt --vault-id prod@~/.vault_prod.txt
```
This way, each environment’s secrets are isolated and encrypted separately.

## 🧩 How Encryption Works
- Ansible Vault uses AES-256 symmetric encryption.
- The same password is used for encrypting and decrypting.
- You can change passwords anytime with ansible-vault rekey.

🧭 Best Practices
✅ Store vault files in Git, but exclude password files (.gitignore).
✅ Use --vault-id for multiple environments.
✅ Combine Vault with Ansible Tower/AWX credentials for secure automation.
✅ Don’t hard-code decrypted variables in playbooks.
✅ Use CI/CD secrets to inject vault passwords dynamically.