# AWS Notes:

---

## 1. Deployment Models: The "Real-World" Explanation

Don't just define them. Explain **who owns the risk** and **where the hardware lives**.

### **On-Premises (Private Data Center)**
* **Simple Concept:** You own the house. You fix the roof, you pay the electric bill, you buy the servers.
* **The Trade-off:** Maximum Control vs. Maximum Headache (CapEx).
* **40LPA Interview Angle:**
    * **Why stay?** Data Sovereignty (Law says data cannot leave the building) or Ultra-Low Latency (Manufacturing robots needing <1ms response).
    * **The Myth:** "On-prem is more secure." **Reality:** Only if your security team is better than AWS's security team (rare).

### **Public Cloud (AWS, Azure)**
* **Simple Concept:** You rent a hotel room. You use the electricity and bed, but you don't fix the plumbing.
* **The Trade-off:** Speed/Agility vs. Variable Cost (OpEx).
* **Key Concept - Multi-Tenancy:** Your VM is on the same physical server as Netflix's VM.
    * *Security Note:* AWS uses the **Nitro System** (specialized hardware cards) to physically separate your memory/CPU from neighbors. This solves the "Noisy Neighbor" and security risks.

### **Private Cloud**
* **Simple Concept:** A hotel floor reserved *only* for you.
* **Confusion Point:** A VPC (Virtual Private Cloud) is **NOT** a Private Cloud. A VPC is logically isolated space on *shared* public hardware.
* **True Private Cloud on AWS:**
    * **Dedicated Hosts:** You rent the specific physical server box in AWS.
    * **AWS Outposts:** AWS ships a rack of servers to *your* building, but AWS manages it remotely.

### **Hybrid Cloud**
* **Simple Concept:** Connecting your House (On-prem) to the Hotel (AWS).
* **The Hard Part:** It’s not the connection; it’s the **Data Gravity**. Apps are heavy; moving data between on-prem and cloud is slow and expensive.
* **Connectivity:** usually via **Direct Connect** (dedicated fiber) or **Site-to-Site VPN** (over internet).

---

## 2. AWS Global Infrastructure: The Physical Layout



Think of the infrastructure like a set of concentric circles.

### **1. Regions (The Geographic Boundary)**
* **What is it?** A separate geographic area (e.g., `us-east-1` N. Virginia, `ap-south-1` Mumbai).
* **Isolation:** Regions are totally isolated. If Mumbai burns down, N. Virginia doesn't know and doesn't care.
* **Design Rule:** Data **never** leaves a Region unless you explicitly copy it (Compliance).

### **2. Availability Zones (AZs) (The HA Hero)**
* **What is it?** A cluster of data centers within a Region.
* **The "Secret Sauce":** AZs are physically separated (flood plains, power grids) but close enough (<100km) to have **single-digit millisecond latency**.
* **Why it matters:** This low latency allows **Synchronous Replication**.
    * *Example:* When you write to an RDS Database in AZ-A, it instantly copies to AZ-B before telling you "Success." This is why Multi-AZ works.

### **3. Local Zones**
* **Simple Concept:** AWS puts a mini-data center in a specific city (e.g., Delhi) that is attached to a parent Region (Mumbai).
* **Use Case:** You need super low latency for video rendering in Delhi, but don't want to manage on-prem servers.

### **4. Edge Locations (Points of Presence)**
* **Simple Concept:** Small server racks in thousands of cities worldwide.
* **Services that live here:**
    * **CloudFront:** Caches images/video closer to users.
    * **Route53:** DNS queries (needs to be fast).
    * **WAF (Web Application Firewall):** Filters bad traffic *before* it hits your main servers.

---

## 3. How "Region-Based" Services Work (Critical for Architects)

This is the difference between a Junior and a Senior engineer. You must understand the **Control Plane** vs. **Data Plane**.

### **The Analogy: The Switch vs. The Lightbulb**
* **Control Plane (The Switch):** The API tools used to configure things. (e.g., "Launch EC2", "Update Security Group", "Create Bucket").
* **Data Plane (The Lightbulb):** The actual running service handling user traffic. (e.g., The EC2 instance running code, the DynamoDB table reading data).

### **What happens when a Region "Fails"?**
Usually, the **Control Plane** fails first.
* *Scenario:* `us-east-1` has an issue. You try to Auto-Scale, but the API says "Error".
* **However**, your *existing* EC2 instances (Data Plane) usually keep running fine!

### **Interview Winner: "Static Stability"**
To survive a Region failure, do not rely on the Control Plane (Auto-scaling) to save you.
> **Strategy:** "We provision enough capacity ahead of time to handle the load if an AZ fails. We don't wait for Auto-Scaling to launch new servers, because during an outage, the 'Launch' API might be broken."

---

## 4. AWS Pricing Calculator & Cost Intelligence

Don't just list prices. Explain **where the money leaks**.

### **The Mental Model for Pricing: "Compute, Storage, & Motion"**
1.  **Compute:** Paying for rent (EC2 per hour, Lambda per ms).
2.  **Storage:** Paying for space (S3 GBs, EBS Volumes).
3.  **Data Transfer (The Silent Killer):** Paying for moving data.

### **The "Hidden" Costs (Interview Trap)**
If asked to estimate a bill, always mention these:
1.  **NAT Gateways:** You pay an hourly fee **PLUS** a fee for every GB of data that goes through it.
    * *Fix:* Use **VPC Endpoints** (Gateway/Interface) to keep S3/DynamoDB traffic local and free.
2.  **Cross-AZ Traffic:** If App-Server (AZ-A) talks to DB-Server (AZ-B), AWS charges ~$0.01/GB.
3.  **EBS Snapshots:** Old backups accumulate and cost money.

### **Example Calculation (Web App)**
* **Web Servers:** 2x `t3.medium` (Reserved Instances for 40% savings).
* **DB:** RDS PostgreSQL (Multi-AZ doubles the cost but essential for HA).
* **Load Balancer:** Application Load Balancer (Fixed hourly + traffic processing fee).

---

## 5. Service Models: IAAS, PAAS, SAAS, CAAS



### **IaaS (Infrastructure as a Service) - "The Mechanic"**
* **You manage:** OS, Patching, Runtime, Code.
* **AWS:** EC2, VPC, EBS.
* **When to use:** You need total control over the Kernel or you are lifting-and-shifting a legacy app that requires specific OS config.

### **PaaS (Platform as a Service) - "The Developer"**
* **You manage:** Just Code & Data. AWS handles the OS/Patching.
* **AWS:** Elastic Beanstalk, RDS, Lambda (FaaS).
* **When to use:** Speed. "I just want to deploy my Node.js app, I don't care about Linux updates."

### **SaaS (Software as a Service) - "The User"**
* **You manage:** Nothing (just settings).
* **AWS:** Amazon Connect (Call center), WorkMail.
* **When to use:** Buy vs Build. Never build your own email server.

### **CaaS (Containers as a Service) - "The Modern Standard"**
This sits between IaaS and PaaS.
* **The Orchestrator (The Brain):**
    * **EKS:** Kubernetes (Standard, complex, portable).
    * **ECS:** AWS Native (Simple, deep AWS integration).
* **The Launch Type (The Muscle):**
    * **EC2 Mode:** You manage the VM worker nodes. (Cheaper, more work).
    * **Fargate:** Serverless containers. AWS manages the worker nodes. (Pricey, zero OS maintenance).

---

## 6. Top Interview Scenarios (In Simple Language)

**Q1: "Explain High Availability (HA) vs. Fault Tolerance (FT)."**
* **HA:** The system minimizes downtime. If Server A dies, Server B takes over after a few seconds. (e.g., RDS Multi-AZ).
* **FT:** The system has *zero* downtime/interruption. (e.g., A RAID array where a disk fails but the data is still readable instantly). FT is much more expensive than HA.

**Q2: "A Junior Dev asks: Why can't I just use one big region for the whole world?"**
* **Answer:** Two reasons.
    1.  **Speed of Light:** A user in India accessing a server in USA will have lag (250ms+). Physics cannot be beaten.
    2.  **Data Laws (GDPR/DPDP):** Indian banking data often legally cannot leave India.

**Q3: "We need to save money. Should we move from EC2 to Serverless (Lambda)?"**
* **Nuance Answer:** "Not always. Lambda is cheap for *spiky* traffic (idle 80% of the time). If you have a high-traffic app running 24/7 at 100% CPU, Lambda is actually **more expensive** than a reserved EC2 instance."