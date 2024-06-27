**Distributed Caching System**

The system consists of three main components: Source End, Client End, and Center.

* **Source End**: Responsible for storing business data. When data changes, it notifies the Center about which keys have been updated.
* **Client End**: The consumer of data. Each client maintains a "consensus-in-progress" status for each key.
	+ When this state is active, the client queries the Center on every data access to check if consensus has been reached.
	+ If not, it uses the previous cached data.
	+ If consensus has been reached, the client uses the new data, and the first client to do so updates the Center's data.
* **Center**: Receives requests from clients and:
	+ Checks if its local cache is stale (dirty). If it is, returns an empty response.
	+ If the local cache is not stale, returns the current cached value.
	+ When receiving update notifications from the Source End, waits for consensus with all clients to ensure everyone agrees on the updated state.
	+ During consensus period, local cache remains in its current state (invalid or valid).
	+ Returns current cached data to client requests during this period.
	+ Only when consensus is reached does the Center update its local cache to reflect the new state.
