## Quick Start

### 1. Clone

```bash
git clone https://github.com/AJ040896/Selenium---LinkedIn.git
cd Selenium---LinkedIn
```

### 2. Run setup script (once)

```bash
bash setup.sh
```

### 3. Fill in your credentials

```bash
nano .env
```

```
LINKEDIN_USERNAME=your-email@example.com
LINKEDIN_PASSWORD=your-password
```

### 4. Run tests

```bash
mvn test
```

## Credential resolution order

1. `-D` CLI flag — `mvn test -DBROWSER=firefox`
2. `.env` file — local development
3. System environment variable — CI/CD pipelines
4. Hardcoded default — non-sensitive fallback only
