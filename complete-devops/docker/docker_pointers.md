# 🐳 Docker Notes

## Containers vs Virtual Machines (VM)

---

### 🧩 Understanding the Components in Hardware and Software

<img width="752" height="692" alt="image" src="https://github.com/user-attachments/assets/a912f9c2-b8c7-4497-9739-a4691c085261" />

When you double-click a file (say `notes.txt`), here’s what happens behind the scenes:

1. **You (User)** interact with the **Operating System (OS)** via GUI or terminal.
2. The **OS** sends your request to the **Kernel**.
3. The **Kernel** converts your request into **hardware-level instructions** for CPU, memory, or disk.

🧠 **The Kernel** acts as a **bridge between hardware and software**.  
You communicate with the kernel using **shell commands**.

---

### 🖥️ Virtual Machines (VM)

<img width="1125" height="402" alt="image" src="https://github.com/user-attachments/assets/12bedec6-1e7f-4402-866a-688a2beede4a" />

- A **Virtual Machine (VM)** is a full virtualized system running on top of physical hardware.  
- Each VM runs its **own Operating System** and **Kernel**.
- Managed by a **Hypervisor** (e.g., VMware, VirtualBox, KVM, Hyper-V).

#### ⚙️ How it works
- The **Hypervisor** understands resource requirements (CPU, RAM, Disk, Network) from each VM.
- It allocates these resources from the physical infrastructure.
- Each VM runs independently and consumes dedicated system resources.


---

### 📦 Containers

- A **Container** is a lightweight, isolated environment to run an application.
- Containers **share the same OS kernel** but have isolated **processes, libraries, and dependencies**.
- Managed by **Container Engines** like:
  - `Docker`
  - `containerd`
  - `CRI-O`

#### ⚙️ How it works
- The **Container Engine** communicates directly with the host OS kernel.
- Containers use **namespaces** and **cgroups** for isolation.
- They don’t need a separate OS — making them faster and more resource-efficient.



---

### ⚖️ VM vs Container Comparison

| Feature | Virtual Machine | Container |
|----------|-----------------|------------|
| OS | Has its own OS | Shares host OS kernel |
| Boot Time | Minutes | Seconds |
| Resource Usage | Heavy | Lightweight |
| Isolation | Strong (full OS-level) | Process-level |
| Portability | Harder (different hypervisors) | Easy (Docker images) |
| Example | VMware, VirtualBox | Docker, containerd |

---

### 🧩 Key Takeaways

- **Hypervisor** → Core component managing VMs and their resources.  
- **Containers** → Share host OS resources efficiently.  
- **Container Engines** like Docker manage the creation, running, and networking of containers.  
- Containers are **faster**, **lighter**, and **easier to deploy** than traditional VMs.

---

# 🐳 Docker Architecture & Components

---

## 🏗️ Docker Architecture Overview

Docker follows a **Client–Server architecture** that consists of:

<img width="1092" height="559" alt="image" src="https://github.com/user-attachments/assets/a18cdc8a-3033-4605-9fe7-7e60abf2c682" />

1. **Docker Client (CLI)**
2. **Docker Daemon (Server)**
3. **Docker Registry (Hub or Private)**
4. **Docker Objects (Images, Containers, Volumes, Networks)**

Workflow - 
1. The **Docker Client (CLI)** sends commands (like `docker run`, `docker build`) to the **Docker Daemon**.
2. The **Docker Daemon** performs heavy lifting — builds, runs, and manages containers.
3. The **Daemon** pulls images from the **Docker Registry** if they aren’t available locally.
4. The **Container** runs using resources from the **Host OS kernel**.
