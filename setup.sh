#!/bin/bash
# ─────────────────────────────────────────────────────────────
# setup.sh — run once after cloning
# Usage: bash setup.sh
# ─────────────────────────────────────────────────────────────

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  LinkedIn Automation Framework — Setup"
echo "═══════════════════════════════════════════════════════"
echo ""

# ── Step 1: Check Java ─────────────────────────────────────
echo "Checking Java..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found. Install Java 11 or higher."
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | head -1)
echo "Found: $JAVA_VERSION"

# ── Step 2: Check Maven ────────────────────────────────────
echo ""
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven not found. Install Maven 3.x."
    exit 1
fi
MVN_VERSION=$(mvn -version 2>&1 | head -1)
echo "Found: $MVN_VERSION"

# ── Step 3: Create .env file ───────────────────────────────
echo ""
echo "Setting up .env file..."
if [ -f ".env" ]; then
    echo ".env already exists — skipping"
else
    cp .env.template .env
    echo ".env created from template"
    echo ""
    echo "─────────────────────────────────────────────────────"
    echo "  ACTION REQUIRED"
    echo "─────────────────────────────────────────────────────"
    echo "  Open .env and fill in your LinkedIn credentials:"
    echo "  LINKEDIN_USERNAME=your-email@example.com"
    echo "  LINKEDIN_PASSWORD=your-password"
    echo "─────────────────────────────────────────────────────"
fi


# ── Step 4: Create reports directory ──────────────────────
echo ""
echo "Creating reports directory..."
mkdir -p target/reports
echo "target/reports/ ready"

# ── Step 5: Install pre-commit hook ───────────────────────
echo ""
echo "Installing pre-commit hook..."
HOOK_PATH=".git/hooks/pre-commit"
cat > "$HOOK_PATH" << 'EOF'
#!/bin/bash
# Blocks commits that accidentally include credential files

BLOCKED_FILES=(
    ".env"
)

for file in "${BLOCKED_FILES[@]}"; do
    if git diff --cached --name-only | grep -q "^${file}$"; then
        echo ""
        echo "╔═══════════════════════════════════════════════╗"
        echo "║  COMMIT BLOCKED — credential file detected   ║"
        echo "╠═══════════════════════════════════════════════╣"
        echo "║  File: ${file}"
        echo "║  This file contains credentials and must     ║"
        echo "║  never be committed to Git.                  ║"
        echo "║                                              ║"
        echo "║  To unstage:                                 ║"
        echo "║  git restore --staged ${file}"
        echo "╚═══════════════════════════════════════════════╝"
        echo ""
        exit 1
    fi
done

exit 0
EOF
chmod +x "$HOOK_PATH"
echo "Pre-commit hook installed — credential files are protected"

# ── Step 6: Maven dependencies ────────────────────────────
echo ""
echo "Downloading Maven dependencies..."
mvn dependency:resolve -q
echo "Dependencies ready"

# ── Done ───────────────────────────────────────────────────
echo ""
echo "═══════════════════════════════════════════════════════"
echo "  Setup Complete!"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "  Next steps:"
echo "  1. Open .env and fill in your LinkedIn credentials"
echo "  2. Run tests: mvn test"
echo "  3. Run headless: mvn test -DHEADLESS=true"
echo "  4. Run specific tag: mvn test -Dcucumber.filter.tags=\"@smoke\""
echo ""