# 🐳 Docker Notes

## Containers vs Virtual Machines (VM)

---

### 🧩 Understanding the Components in Hardware and Software

When you double-click a file (say `notes.txt`), here’s what happens behind the scenes:

1. **You (User)** interact with the **Operating System (OS)** via GUI or terminal.
2. The **OS** sends your request to the **Kernel**.
3. The **Kernel** converts your request into **hardware-level instructions** for CPU, memory, or disk.

🧠 **The Kernel** acts as a **bridge between hardware and software**.  
You communicate with the kernel using **shell commands**.

---

### 🖥️ Virtual Machines (VM)

- A **Virtual Machine (VM)** is a full virtualized system running on top of physical hardware.  
- Each VM runs its **own Operating System** and **Kernel**.
- Managed by a **Hypervisor** (e.g., VMware, VirtualBox, KVM, Hyper-V).

#### ⚙️ How it works
- The **Hypervisor** understands resource requirements (CPU, RAM, Disk, Network) from each VM.
- It allocates these resources from the physical infrastructure.
- Each VM runs independently and consumes dedicated system resources.

#### 🧱 Example Stack
