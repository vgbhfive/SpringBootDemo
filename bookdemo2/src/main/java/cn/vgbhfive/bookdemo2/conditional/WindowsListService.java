package cn.vgbhfive.bookdemo2.conditional;

/**
 * @time:
 * @author: Vgbh
 */
public class WindowsListService implements ListService {
    @Override
    public String showListCmd() {
        return "dir";
    }
}
