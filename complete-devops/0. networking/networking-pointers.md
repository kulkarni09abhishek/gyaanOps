# 🌐 Networking Notes

## 🖥️ Basics

### Network
A **network** is a collection of computers connected to each other to share resources and data.

### Internet
The **Internet** is a collection of interconnected computer networks.

### Protocol
A **protocol** is a standardized set of rules that define how devices format, transmit, and receive data — acting as a common language for communication.

### WWW (World Wide Web)
The **World Wide Web (WWW)** is a global collection of documents and other resources, linked by hyperlinks and URIs.

### Internet Society (ISOC)
The **Internet Society (ISOC)**, founded in 1992, is dedicated to keeping the Internet open, transparent, and user-defined.

---

## 🧩 Simple Client-Server Model
A **client** (like a browser or app) sends a request to a **server**, which processes it and sends a response back.
<img width="2440" height="988" alt="image" src="https://github.com/user-attachments/assets/1fb69aff-e28b-424e-a92d-83ef6399493c" />

---

## 📡 Network Protocols

### TCP (Transmission Control Protocol)
Used for **reliable** and **ordered** delivery (e.g., web browsing, emails, file transfers).  
Ensures no data loss.

### UDP (User Datagram Protocol)
Used for **speed-critical**, real-time apps (e.g., gaming, streaming, video calls).  
Faster but **unreliable**.

### HTTP / HTTPS
- **HTTP**: Used for transferring non-sensitive web content.  
- **HTTPS**: Secure version (uses SSL/TLS).  
Used for web communication between browsers and servers.

---

## 🌍 ISP (Internet Service Provider)
An **ISP** connects users to the internet.  
Your device connects → ISP → Internet backbone → Data travels → Response returns via the **last mile** (ISP to your home).
<img width="750" height="328" alt="image" src="https://github.com/user-attachments/assets/62b99dac-3367-49a5-92f8-66b10d08e32f" />


### NAT (Network Address Translation)
Allows multiple devices on a private network to share a single public IP address.  
Adds a layer of security and conserves IPv4 addresses.
<img width="1024" height="576" alt="image" src="https://github.com/user-attachments/assets/c84c98db-a5e1-4506-a8a3-f76791cf4181" />


---

## 🧠 IP & Ports

- **IP Address** → Identifies the device.  
- **Port Number** → Identifies the application.  
- Total Ports: `2^16 ≈ 65,000`.

### Common Ports
| Service | Port |
|----------|------|
| FTP | 20/21 |
| SSH | 22 |
| HTTP | 80 |
| HTTPS | 443 |
| Jenkins/Web Apps | 8080 / 8443 |
| Docker | 2375 / 2376 |
| K8s API | 6443 |
| Prometheus | 9090 |
| Elasticsearch | 9200 |
| Kibana | 5601 |
| MySQL | 3306 |
| PostgreSQL | 5432 |

---

## 🕸️ Types of Networks

| Type | Description |
|------|--------------|
| **LAN** | Connects devices within a limited area (home/office). |
| **MAN** | Connects multiple LANs within a city/metropolitan area. |
| **WAN** | Connects networks over large geographic areas. |

---

## ⚙️ Devices

### Modem
Converts **digital** signals to **analog** (and vice versa) to connect local networks to the internet.

### Router
Connects multiple devices to the same network and routes data between them and the internet.

---

## 🧱 Network Topologies
- **Bus**
- **Ring**
- **Star**
- **Tree**
- **Mesh**

---

# 🧬 OSI Model (7 Layers)

Sending a WhatsApp message “Hey!” from India to the US:

| Layer | Name | Function | Data Unit | Example Protocols |
|-------|------|-----------|------------|--------------------|
| 7 | **Application** | Message creation (WhatsApp App, HTTPS) | Message data | HTTPS, WebSocket |
| 6 | **Presentation** | Encryption & compression | Encrypted blob | AES, Curve25519 |
| 5 | **Session** | Maintains persistent connection | Session stream | TLS, WebSocket |
| 4 | **Transport** | Reliable delivery, segmentation | TCP segments | TCP, UDP |
| 3 | **Network** | Routing and addressing | Packets | IP, NAT, ICMP |
| 2 | **Data Link** | Framing, error detection | Frames | Wi-Fi, Ethernet |
| 1 | **Physical** | Bit transmission | Bits | Fiber, Wi-Fi |

### Example:
Your phone → Router → Internet → WhatsApp Server → Friend’s phone  
Each layer adds its own header (encapsulation).  
On receiving, the reverse happens (de-encapsulation).

---

## 🔒 End-to-End Example Summary
| Component | Layer | Technology |
|------------|--------|------------|
| Encryption | 6/7 | Signal Protocol |
| Reliable Transfer | 4 | TCP |
| Routing | 3 | IP, BGP |
| Local Transmission | 2 | Wi-Fi, LTE |
| Physical Medium | 1 | Fiber, 4G/5G |

---

## ⚙️ TCP/IP Model
Simplified version of OSI with **5 layers** — combines application, presentation, and session into one.

---

## 🧩 MAC Address
A **MAC address** is a unique hardware ID assigned to a network card (Layer 2).  
Used by **switches** to forward frames in a local network.

---

<img width="889" height="500" alt="image" src="https://github.com/user-attachments/assets/730aa05e-05bf-4a26-8e5f-f0e4cda33b7e" />

## 🍪 Cookies
A **cookie** is a small piece of data stored by the browser when you first log in — used for session management.

---

## 🌍 DNS (Domain Name System)
A **distributed database** that resolves domain names to IP addresses.  
Lookup order:
1. Local DNS cache  
2. ISP DNS  
3. Root / Authoritative DNS  

---

## 🤝 TCP 3-Way Handshake

| Step | Direction | Purpose | TCP State |
|------|------------|----------|------------|
| 1. SYN | Client → Server | Initiate connection | SYN_SENT |
| 2. SYN-ACK | Server → Client | Acknowledge request | SYN_RECEIVED |
| 3. ACK | Client → Server | Confirm connection | ESTABLISHED |

**Analogy:**  
You: “Hello?” → Friend: “Hey, can you hear me?” → You: “Yes, loud and clear!” → Start chatting 🎉

---

## 🔁 Data Representation at Layers
- Layer 1 → **Bits**
- Layer 2 → **Frames**
- Layer 3 → **Packets**
- Layer 4 → **Segments**
- Layer 5–7 → **Data / Messages**

<img width="447" height="227" alt="image" src="https://github.com/user-attachments/assets/8da30bcc-cd99-45ae-bb7a-96791bec93a4" />

---

## 🧭 Loopback Address

| Version | Address | Range | Description |
|----------|----------|--------|-------------|
| IPv4 | 127.0.0.1 | 127.0.0.0/8 | Localhost |
| IPv6 | ::1 | ::1/128 | IPv6 localhost |

**Usage:**  
Used to test network functionality within your own computer (packets never leave the device).

🧠 How It Works
When you send a packet to the loopback address (127.0.0.1):
	• The packet never leaves your computer.
	• It is handled entirely by the operating system’s network stack.
This helps you test local network functionality (like sockets, web servers, APIs) without needing an external network or internet.

**Interview Tip:**  
> A loopback address (127.0.0.1) routes traffic internally to test local network functionality.

---

✅ **In Short:**
When you send a WhatsApp message:
1. App encrypts and prepares data (Application layer).
2. Layers add addressing, routing, and framing info.
3. Data travels through routers and ISPs to the destination.
4. Receiver’s device reverses the process and decrypts the message.

---

📘 **Summary Table**

| Concept | Layer | Function |
|----------|--------|-----------|
| Encryption | 6/7 | Data security |
| Reliability | 4 | TCP delivery |
| Routing | 3 | IP addressing |
| Transmission | 2 | Frames |
| Signal | 1 | Bits |

---
