package repo;

import java.io.IOException;

/**
 * 继承IOException，在新建版本库时，如果版本库文件已经存在会抛出这个异常，
 * 图形化界面接收到这个异常时会弹出对话框告知用户版本库已经存在。
 */
public class RepoAlreadyExistException extends IOException {
    public RepoAlreadyExistException() {
        super("already exist");
    }
}
