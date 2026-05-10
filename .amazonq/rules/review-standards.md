# Amazon Q Review Standards

## Suppression Rules

### Array index without bounds check inside runCatching
**Pattern:** Flagging `list[index]` as a crash risk when the call is already inside a `runCatching { }` block (or equivalent try/catch) in the caller.

**Reason to suppress:** Any `IndexOutOfBoundsException` thrown inside `runCatching` is caught and returned as `Result.failure`. The app produces an error state rather than a crash. This is the intended error-handling pattern in this codebase.

**Example (rejected finding):** `token.split(".")[1]` in `Token.fromObtainKrakenToken`, called from `ApolloGraphQLEndpoint.obtainKrakenToken` which wraps the call in `runCatching`.

Do not flag array/list index access as a crash risk unless the access is provably outside any exception-catching scope.
