package ywd.action;

import ywd.exception.YwdException;
import ywd.util.PrintUtils;


public class HomeAction extends Action {

    /** For home page */
    public String execute() {
        try {
        } catch(Exception e) {
            PrintUtils.print(this.getClass(), new YwdException(e));
            return ERROR;
        }
        return SUCCESS;
    }

}
