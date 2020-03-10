package jalikejdbc;

public interface ErrorMessage {
    String CONNECTION_POOL_IS_NOT_YET_INITIALIZED = "Connection pool is not yet initialized.";

    String CANNOT_START_A_NEW_TRANSACTION = "Cannot start a new transaction.";

    String TRANSACTION_IS_NOT_ACTIVE = "Transaction is not active.";

    String IMPLICIT_DB_INSTANCE_REQUIRED = "An instance of scalikejdbc.DB is required implicitly.";
}
