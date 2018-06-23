package cn.vgbhfive.bookdemo2.conditional;

/**
 * @time:
 * @author: Vgbh
 */
public class LinuxListService implements ListService {
    @Override
    public String showListCmd() {
        return "ls";
    }
}
