# Amazon Q Review Standards

## Suppression Rules

### Array index without bounds check inside runCatching
**Pattern:** Flagging `list[index]` as a crash risk when the call is already inside a `runCatching { }` block (or equivalent try/catch) in the caller.

**Reason to suppress:** Any `IndexOutOfBoundsException` thrown inside `runCatching` is caught and returned as `Result.failure`. The app produces an error state rather than a crash. This is the intended error-handling pattern in this codebase.

**Example (rejected finding):** `token.split(".")[1]` in `Token.fromObtainKrakenToken`, called from `ApolloGraphQLEndpoint.obtainKrakenToken` which wraps the call in `runCatching`.

Do not flag array/list index access as a crash risk unless the access is provably outside any exception-catching scope.

### Redundant null check after lastOrNull predicate
**Pattern:** Flagging `if (result?.field != null)` as redundant when `result` came from `lastOrNull { it.field != null }` wrapped in `runCatching { }.getOrNull()`.

**Reason to suppress:** The outer null check is NOT redundant. If `runCatching` catches an exception, `getOrNull()` returns null regardless of what `lastOrNull` would have returned. The check also enables Kotlin smart casting to a non-nullable type inside the if-block.

Do not flag defensive null checks as redundant when the value originates from a `runCatching { }.getOrNull()` chain.

### Integer display format for meter readings
**Pattern:** Flagging `value.toInt()` or `displayValue.toInt()` in UI display code as "data loss" for meter reading values.

**Reason to suppress:** This codebase intentionally displays meter readings as whole-number kWh (e.g. "23800") for UI clarity. This is a deliberate display choice, not a bug. The underlying `Double` precision is preserved in the domain model; only the UI label is rounded.

Do not flag `.toInt()` on meter reading display values as data loss.
