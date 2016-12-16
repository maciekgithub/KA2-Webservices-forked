package pl.euler.bgs.restapi.core.management;

/**
 * Enum describes the mode of the maintenance process.
 */
public enum MaintenanceTriggerMode {
    /** Wait until all transactions finish and then suspend the pool. */
    NORMAL,
    /** Kill all connections and cancel transactions immediately.  */
    IMMEDIATE
}
