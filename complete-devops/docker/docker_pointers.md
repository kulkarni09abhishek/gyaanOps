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


# ⚙️ Control Groups (cgroups) in Docker

## 🧩 1. What Are cgroups?

**cgroups** (short for **Control Groups**) are a **Linux kernel feature** that Docker uses to **limit**, **isolate**, and **monitor** the **resource usage** (CPU, memory, disk I/O, network, etc.) of containers.

They ensure that each container gets its **fair share of system resources**, preventing one container from consuming everything and affecting others.

---

## 🧠 2. Why cgroups Are Important

Docker uses **cgroups** to:

| Purpose | Description |
|----------|-------------|
| **Resource Limiting** | Restrict how much CPU, memory, or disk I/O a container can use. |
| **Resource Isolation** | Ensure containers don’t interfere with each other’s resources. |
| **Resource Accounting** | Track and report resource usage for monitoring and billing. |
| **System Stability** | Prevent a single container from crashing the entire host. |

---

## 🧱 3. Relationship Between Docker, cgroups, and Namespaces

Docker relies on two main Linux kernel features for containerization:

| Component | Function |
|------------|-----------|
| **Namespaces** | Provide isolation (process, network, filesystem, etc.) |
| **cgroups** | Provide resource control (limit and monitor usage) |

Together, they make containers **lightweight**, **isolated**, and **resource-controlled**.

---

## ⚡ 4. How Docker Uses cgroups

When you run a Docker container, Docker creates a **new cgroup** for that container.

This cgroup tracks and enforces limits such as:
- **CPU quota** (how much processing power the container can use)
- **Memory limit**
- **Block I/O priority**
- **Network bandwidth** (in advanced configurations)

Each container gets its own directory under: /sys/fs/cgroup/

## 🧰 5. Example: Setting Resource Limits Using cgroups
```bash
docker run -d --name cpu-demo --cpus="0.5" ubuntu:latest sleep 1000
```
👉 Limits the container to 50% of one CPU core.


# 🧠 PID & Namespaces in Docker
## 🧩 1. What Are Namespaces?

**Namespaces** are a core **Linux kernel feature** that Docker uses to **isolate containers** from each other and from the host system.

Each namespace provides **a separate view of system resources**, ensuring that processes inside a container only “see” their own environment — not the host or other containers.

> Think of namespaces as “walls” that separate containers, while **cgroups** control how much resource they can use.

---

## ⚙️ 2. Types of Namespaces Used by Docker

Docker uses several Linux namespaces together to create isolated containers:

| Namespace | Isolates | Example |
|------------|-----------|----------|
| **PID** | Process IDs | Each container has its own process tree |
| **NET** | Network interfaces, ports, routing tables | Each container has its own virtual network |
| **MNT** | Filesystem mount points | Container sees only its own filesystem |
| **UTS** | Hostname and domain name | Containers can have unique hostnames |
| **IPC** | Inter-process communication | Containers have isolated shared memory |
| **USER** | User and group IDs | Containers can map users differently than the host |

---

## 🧩 3. PID Namespace

### 🧠 What It Does:
The **PID namespace** provides **process isolation**.  
Each container gets its own set of **process IDs (PIDs)**, starting from **PID 1** inside that container.

So, even though the container’s main process is PID `1` inside the container, it might be PID `12345` on the host.

---

### 🧪 Example:

#### Run a container and check its PID:
```bash
docker run -d --name pid-demo ubuntu sleep 1000
```

Check the PID on the host:
```bash
docker inspect --format '{{.State.Pid}}' pid-demo
```
👉 This is the real PID on the host.

Now enter the container:
```bash
docker exec -it pid-demo bash
ps -ef
```
👉 The same process appears as PID 1 (root PID) inside the container, even though it’s PID is different on the host.


---
# 🐳 Docker Command Deep Dive
---

## 🧩 `docker run`

**Description:**
Creates and starts a new container from a specified Docker image.  
It’s equivalent to running `docker create` + `docker start`.

**Syntax:**
```bash
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

| Option   | Description                                             |
| -------- | ------------------------------------------------------- |
| `-d`     | Run container in **detached** mode (background).        |
| `-p`     | Map **host port** to **container port** (`-p 8080:80`). |
| `--name` | Assign a custom **container name**.                     |
| `-v`     | Mount a **volume** (`-v host_path:container_path`).     |
| `-e`     | Set **environment variables**.                          |
| `--rm`   | Automatically remove the container when it stops.       |
| `-it`    | Run container in **interactive terminal** mode.         |

**When we run a container using docker run, it will exits immediately after a process/task inside container is completed.**

```bash
# Run nginx container in background and map port 8080 of host to 80 of container
docker run -d -p 8080:80 nginx

# Run custom images (by default docker hub public images and latest tag is used) 
docker run <dockerhub_user_id>/<image_name>:<tag> 

# Run ubuntu container with command (container will exit after command is completed)
docker run ubuntu sleep 5 

# To only Pull nginx image from docker hub
docker pull nginx

# Run ubuntu container interactively with shell access
docker run -it ubuntu /bin/bash

# Run and auto-remove container after execution
docker run --rm alpine echo "Hello, Docker!"

# List running containers
docker ps

# Show all containers (running + stopped)
docker ps -a

# List only container IDs
docker ps -q

# Remove a stopped container
docker rm my_container

# Remove multiple containers
docker rm container1 container2

# Remove all stopped containers
docker rm $(docker ps -aq)

# Force remove a running container
docker rm -f my_container

#list docker images
docker images

# Remove image by name
docker rmi nginx

# Remove image by ID
docker rmi 7e4d58f0e5f3

# Force remove image
docker rmi -f nginx

# Remove all unused images
docker image prune -a

# Run bash inside a running container
docker exec -it my_container /bin/bash

# Check environment variables inside container
docker exec my_container env

# Run a background process inside container
docker exec -d my_container touch /tmp/newfile

# Stop a running container
docker stop my_container

# Stop multiple containers
docker stop container1 container2

# Stop all running containers
docker stop $(docker ps -q)

# Attach to a running container
docker attach my_container

# Safely detach without stopping container (use these keys)
Ctrl + P + Q

# To get more details about container 
docker inspect <container_name>

# To get logs of container
docker logs <container_name>

```

**COPY vs ADD**
copy will work only for source and destination files present on host.
add has all functionalities as copy and additional functionality to download.
<img width="936" height="133" alt="image" src="https://github.com/user-attachments/assets/415996d5-60db-431a-8183-d95e2dcc63e0" />
<img width="936" height="133" alt="image" src="https://github.com/user-attachments/assets/caf7a785-8e6f-49e7-8d2e-013469a71367" />
<img width="713" height="457" alt="image" src="https://github.com/user-attachments/assets/1a46a6b3-a187-4f23-9401-ff940967e9cc" />


---
# 🐳 Docker Storage: Bind Mounts vs Volume Mounts
---

Docker containers are **ephemeral** — meaning their data is lost when the container stops or is removed.  
To persist data, Docker provides **mounts**, which allow data to live **outside** the container’s writable layer.

There are two main types of mounts used for persistence:

- **Bind Mounts**
- **Volumes**

---

## 📁 1. Bind Mounts

### 🔹 Concept
Bind mounts allow you to **map a specific directory or file from your host machine** into a container.  
This means changes made on the host are **immediately visible inside the container**, and vice versa.

Bind mounts are **tightly coupled with the host system’s directory structure**.

### 🔹 Use Case

- When you want to **share source code** or configuration files between your local system and a container.
- Useful during **development**.

### 🔹 Example

```bash
# Run an Nginx container and bind mount a local HTML directory
docker run -d \
  --name nginx-bind \
  -p 8080:80 \
  -v /home/user/website:/usr/share/nginx/html \
  nginx
```

## 📁 2. Volume Mounts

### 🔹 Concept
Volumes are managed entirely by Docker and stored in Docker’s internal directory (usually /var/lib/docker/volumes).
Unlike bind mounts, you don’t need to know or manage the exact location on the host.

Volumes are more portable, safer, and recommended for production.

Bind mounts are **tightly coupled with the host system’s directory structure**.

### 🔹 Use Case

- For databases or applications where persistent, structured data storage is required.
- Ideal when you don’t want to depend on host file paths.

### 🔹 Example

```bash
# Create a named volume
docker volume create mydata

# Run a MySQL container using that volume
docker run -d \
  --name mysql-container \
  -e MYSQL_ROOT_PASSWORD=root \
  -v mydata:/var/lib/mysql \
  mysql:latest
```

### 🔹 Useful Commands
```bash
# List all volumes
docker volume ls

# Remove unused volumes
docker volume prune

# Remove specific volume
docker volume rm mydata

# Remove container but keep volume
docker rm <container-name>

# Remove container and its associated volume
docker rm -v <container-name>
```



---
# 🐳 Docker Images
---
Docker uses a **layered architecture** to build, share, and run containers efficiently.  
Each image and container in Docker is built on top of a **stack of layers**, which provides **reusability, efficiency, and portability**.

<img width="1435" height="760" alt="image" src="https://github.com/user-attachments/assets/48e80698-167c-40cf-bda1-dd87238a7ef2" />

## 🧩 Understanding Layers

A **Docker image** is made up of **multiple read-only layers** stacked on top of each other.

Each **layer** represents a **set of filesystem changes** (like adding files, installing packages, or modifying configurations).

When you **run a container**, Docker adds a **read-write layer** on top of these image layers, so that the container can make temporary changes.

<img width="973" height="590" alt="image" src="https://github.com/user-attachments/assets/06080ad9-f49b-4b26-b288-2b12d5a9a67e" />

🔹 Image Layers (Read-only)
All layers below the topmost are immutable and shared between containers.

🔹 Container Layer (Read-Write)
When a container is started from an image, Docker adds a thin writable layer on top.
All modifications (file creation, updates, deletions) happen in this container layer.


## ⚙️ Copy-on-Write (CoW) Mechanism

**Copy-on-Write (CoW)** is the technique Docker uses to optimize how files are stored and modified across layers.

---

### 🧠 Concept

When a container modifies a file that exists in one of the lower (read-only) image layers:

<img width="950" height="536" alt="image" src="https://github.com/user-attachments/assets/6f8369e7-5c70-4731-af0e-2524ed7a3af0" />

1. Docker **copies that file** from the **read-only layer** into the **container’s writable layer**.  
2. The container then **modifies the copy**, not the original file.  
3. Other containers sharing the same base image **still see the original unmodified file**.

This approach prevents unnecessary data duplication and helps **save disk space** while maintaining **image immutability**.

---



## 🧩 Caching in Layers
Docker uses build cache to avoid re-executing unchanged steps.

<img width="1429" height="709" alt="image" src="https://github.com/user-attachments/assets/9a114416-b1e7-4d82-b4d0-c4633ba88d4a" />


To force a rebuild:

```bash
docker build --no-cache -t myapp .
```

### 🔹Useful Commands

```bash
# View image history (layer-wise)
docker history <image_name>

# Example
docker history nginx:latest

# Inspect image details
docker inspect <image_name>

# List all image layers
docker image ls
```

---
# 🚀 Docker Image Lifecycle: From Dockerfile to Docker Hub
---

This guide explains the **complete flow** of how Docker builds and publishes images:

> **Dockerfile → docker build → docker tag → docker push**

---

## 🧩 1. Dockerfile – The Blueprint

A **Dockerfile** is a text file that contains **instructions** to build a Docker image.

Each line in the Dockerfile represents a **step/layer** in the image build process.

### 🧠 Example: `Dockerfile`

```dockerfile
# 1️⃣ Base Image
FROM python:3.10-slim

# 2️⃣ Set Working Directory
WORKDIR /app

# 3️⃣ Copy Project Files into Container
COPY . /app

# 4️⃣ Install Dependencies
RUN pip install -r requirements.txt

# 5️⃣ Define Default Command
CMD ["python", "app.py"]
```

## 🏗️ 2. docker build – Build the Image
Once the Dockerfile is ready, use docker build to create an image.
```bash
docker build -t myapp:1.0 .
```


## 🏷️ 3. docker tag – Tagging the Image for Push
Before pushing to a registry (like Docker Hub), tag the image with your repository name.

```bash
docker tag myapp:1.0 username/myapp:1.0
```

## ☁️ 4. docker push – Upload Image to Docker Hub
Step 1: Login to Docker Hub
```bash
docker login
```
You’ll be prompted for your Docker Hub username and password/token.

Step 2: Push the Image
```bash
docker push username/myapp:1.0
```

🔹 Output Example
```yaml
The push refers to repository [docker.io/username/myapp]
d4bce7fd68c1: Pushed
8f7eea4a74c5: Pushed
1.0: digest: sha256:e6a6e8c82a5a... size: 1234
```

**Important points:**
•	It is not mandatory to keep file name as Dockerfile, we can keep any name and pass it while building the image with ‘-f <file_name>’ 
•	When we create an image then data will be stored in cache memory and if we create the same image again then cache memory will be used, and image will be created in less time. If we don’t want to use cache memory, we can pass the flag ‘--no-cache’
•	Env vs. ARG – 
arg will be available only while building container and not inside the container.
env will be available inside the container as well.
•	Use build-arg to pass ARG values to Dockerfile -
# docker build -t <image_name:tag> -f <Dockerfile_location> --build-arg JAVA_VERSION=18-jdk 
•	Use flag ‘--progress=plain’ to see detailed output of docker build.
•	Pass env variables – 
# docker run -e USER_NAME=admin -e PASSWORD=pwd --name test -d nginx
•	Dangling images are Docker images that exist but are not tagged or referenced by any container.
# docker images -f "dangling=true"
•	docker run vs docker start –
docker run – Creates and starts a new container from an image. If the container does not exist, it will create one from the specified image. 
docker run = docker create + docker start
start – Starts an existing stopped container. Cannot create a new container; it only works on containers that were previously created.



---
# ⚙️ Docker Environment Variables, CMD, and ENTRYPOINT
---

In Docker, **environment variables**, **CMD**, and **ENTRYPOINT** control how containers run and behave.  
They define **configuration**, **default behavior**, and **startup commands** for your container.

---

## 🌍 1. Environment Variables in Docker

### 🔹 What are Environment Variables?

Environment variables store **configuration values** (like database URLs, API keys, or credentials)  
that can be **passed into containers** without hardcoding them in the image.

They are key–value pairs available inside the container’s runtime environment.

---

### 🧱 Defining Environment Variables

You can define them in multiple ways:

#### **a) Using `ENV` in Dockerfile**

```dockerfile
FROM node:18
WORKDIR /app
ENV APP_ENV=production
ENV PORT=8080
COPY . .
CMD ["node", "server.js"]
```

#### **b) Using --env flag in docker run**
```bash
docker run -d \
  --name myapp \
  -e APP_ENV=development \
  -e PORT=5000 \
  myimage:latest
```

#### **c) Using an Environment File**
Create a .env file:
```ini
APP_ENV=production
PORT=8080
DB_USER=admin
DB_PASS=secret
```
Then pass it to the container:
```bash
docker run -d --env-file .env myimage:latest
```

🧠 Inspect Environment Variables
```bash
docker exec -it myapp env
```


## ⚙️ 2. CMD Instruction

### 🔹 Purpose
- **Defines default command or arguments** to execute when a container starts.  
- It can be **overridden** at runtime using `docker run`.

---

### 🧱 Syntax

#### **Exec Form (Recommended)**
```dockerfile
CMD ["executable", "param1", "param2"]
```

<img width="802" height="1365" alt="image" src="https://github.com/user-attachments/assets/dca51bf2-765e-4bca-a67a-ec6441d64289" />

## 🚀 3.ENTRYPOINT Instruction
🔹 Purpose
- Defines a fixed executable that will always run when the container starts.
- Used when you want your container to behave like a specific command.

### 🧱 Syntax

#### **Exec Form (Recommended)**
```dockerfile
ENTRYPOINT ["executable", "param1", "param2"]
```

<img width="745" height="1209" alt="image" src="https://github.com/user-attachments/assets/599943c6-8fb3-4c55-84ff-e0fce51fe8c1" />

Generally, we use combination of both.

<img width="326" height="752" alt="image" src="https://github.com/user-attachments/assets/e0be3b98-e531-44fa-8a61-26062c82efb4" />



# 🌐 Docker Networking

---

First things first –
Before installing docker (only ethernet0 is present) –
<img width="1007" height="494" alt="image" src="https://github.com/user-attachments/assets/3a480d97-6cee-4e2c-b732-85385fcea1f0" />

After installing docker (docker0 got added) –
<img width="767" height="572" alt="image" src="https://github.com/user-attachments/assets/58e95419-c89f-462f-a46e-7487ed31e382" />

**When containers want to access the internet, they will first send request to docker0 then docker0 will communicate with et0 (ethernet) and then et0 will pass the request to internet. Hence docker0 acts as a bridge network between containers and et0.**


## 🧩 1. Introduction

Docker networking allows containers to **communicate** with each other, with the **host machine**, and with the **outside world**.  
It provides **isolation**, **connectivity**, and **flexibility** in how applications are deployed.

Every Docker container is attached to a **network** — either the default one created by Docker or a custom network you define.

<img width="1415" height="732" alt="image" src="https://github.com/user-attachments/assets/25639568-cb82-483a-8c0e-4c720578afdc" />

---

## ⚙️ 2. Why Docker Networking?

Docker networking is designed to:
- Enable containers to **talk to each other**.
- Control **how** they communicate (isolated, exposed, bridged, etc.).
- Allow **external access** (to expose container ports to the host or internet).
- Support **multi-container microservices** architecture.

---

## 🧱 3. Docker Network Architecture Overview

When Docker is installed, it automatically creates several networks:

| Network Name | Driver | Purpose |
|---------------|---------|----------|
| **bridge** | bridge | Default network for standalone containers |
| **host** | host | Shares host’s network stack |
| **none** | null | No network connectivity |
| **overlay** | overlay | Used for multi-host (Swarm) networking |
| **macvlan** | macvlan | Assigns MAC address directly to container (acts as physical device) |

You can list all networks:
```bash
docker network ls
```

## 🧩 4. Types of Docker Networks

🔹 1. Bridge Network (Default)
- Created by Docker automatically (docker0).
- Containers on the same bridge can communicate with each other using container names.
- Provides NATed access to the external world through the host.

```bash
docker run -d --name web1 nginx
docker run -d --name web2 nginx
docker exec -it web1 ping web2
```
✅ web1 can reach web2 via internal IP or name.



<img width="1354" height="520" alt="image" src="https://github.com/user-attachments/assets/98242d57-32ab-4a56-a1e0-728e32a8567a" />

To create a custom bridge network:

```bash
docker network create my_bridge
```

Run containers on it:
```bash
docker run -d --name app1 --network my_bridge nginx
docker run -d --name app2 --network my_bridge alpine ping app1
```
✅ Containers on the same custom bridge can resolve names automatically via Docker’s embedded DNS.

<img width="1300" height="501" alt="image" src="https://github.com/user-attachments/assets/4cbee2f9-ba3d-48a6-8d07-b972ebbff8c3" />

<img width="1048" height="658" alt="image" src="https://github.com/user-attachments/assets/4e901808-341e-4f0a-8a74-89a5eb623e4c" />


🔹 2. Host Network
- Container shares the host’s network namespace.
- No network isolation — it uses the host IP directly.
- Improves performance for networking-intensive apps.

```bash
docker run -d --network host nginx
```

✅ Nginx runs on host’s IP and port (e.g., http://localhost:80).
⚠️ Not recommended for production due to lack of isolation.

🔹 3. None Network
- Provides complete network isolation.
- The container has no external connectivity — only the loopback interface (lo).

```bash
docker run -it --network none ubuntu bash
```

## 🧠 5. Docker DNS and Name Resolution
- Docker provides a built-in DNS server for container name resolution.
- Containers on the same user-defined bridge or overlay network can resolve each other by name.


## 🧩 6. Exposing and Publishing Ports
<img width="452" height="488" alt="image" src="https://github.com/user-attachments/assets/5333c953-12f2-4dc0-9c85-d83c8d70cfb1" />


🔍 Quick Reference
```bash
# List all networks
docker network ls

# Create a network
docker network create my_net

# Run container on custom network
docker run -d --name web --network my_net nginx

# Inspect network
docker network inspect my_net

# Connect/disconnect containers
docker network connect my_net app
docker network disconnect my_net app
```


## USER and WORKDIR 
<img width="1033" height="1152" alt="image" src="https://github.com/user-attachments/assets/6f27bfe6-8d07-412d-87b3-b68ebcacc5fb" />

<img width="1037" height="1072" alt="image" src="https://github.com/user-attachments/assets/c55f18df-2e8e-4984-a97e-705c9edfbd97" />
<img width="933" height="258" alt="image" src="https://github.com/user-attachments/assets/ad878ee5-de32-43f1-a6ac-c76f3b1b0bc6" />

Chained WORKDIR commands –
<img width="583" height="487" alt="image" src="https://github.com/user-attachments/assets/7806a684-bf86-4518-907e-04e34e6adbbf" />

## Multistage build
It is used to reduce the image size.
To check layerwise size –
<img width="1125" height="289" alt="image" src="https://github.com/user-attachments/assets/88b3fc50-99b2-4f25-965f-da336ab58dda" />

Multistage -
<img width="529" height="390" alt="image" src="https://github.com/user-attachments/assets/a1543dea-d255-47c6-b844-01c60f74c634" />

- Using distroless images as base image we can further reduce the image size.
- Redhat provides ubi image which is an alternative to distroless images.


