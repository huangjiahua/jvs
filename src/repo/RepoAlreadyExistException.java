package repo;

import java.io.IOException;

public class RepoAlreadyExistException extends IOException {
    public RepoAlreadyExistException() {
        super("already exist");
    }
}
