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

You declare the expected state: nginx should be present
Ansible automatically determines how to reach that state.


## Infrastructure as Code (IaC) vs Configuration Management vs Orchestration
IaC refers to provisioning and managing infrastructure (servers, networks, load balancers, etc.) using code.  
It focuses on **creating** infrastructure from scratch in a consistent and reproducible manner.
Examples:
- Terraform
- AWS CloudFormation
- Azure Bicep

Key capabilities:
- Provisioning servers, networks, storage
- Version-controlled infrastructure definitions
- Immutable infrastructure patterns

Configuration Management tools **configure and maintain** the state of existing servers or services.  
They ensure software, packages, and settings remain consistent over time.

Examples:
- Ansible
- Chef
- Puppet
- SaltStack

Key capabilities:
- Install and configure software
- Manage OS packages & services
- Maintain desired configuration state
- Apply changes incrementally


Orchestration automates **complex workflows and coordination** of resources across multiple systems, especially in distributed environments.  
It ensures **ordering, dependencies, scaling, and lifecycle management**.

Examples:
- Kubernetes
- Docker Swarm
- Apache Mesos
- AWS ECS

Key capabilities:
- Container scheduling
- Auto-scaling & self-healing
- Service discovery & load balancing
- Rolling updates & multi-node coordination


## Quick Comparison

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




