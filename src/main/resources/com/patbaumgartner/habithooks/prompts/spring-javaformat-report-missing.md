Spring Java Format failed before habit-hooks could capture formatter output. Without that output, the harness cannot separate ordinary style drift from formatter setup trouble.

**How to fix it:**

1. Run the configured Spring Java Format goal directly.
2. Confirm habit-hooks can write to the configured report path.
3. Apply formatting or fix the plugin setup, then run habit-hooks again.

Formatter feedback should be quick, boring, and reliable.
