package com.mzdd.dolphinscheduler.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * 操作git工具类
 *
 * @author mzdd
 * @create 2023-03-03 10:34
 */
@Slf4j
public class GitUtil {

    /**
     * 克隆仓库
     *
     * @param file
     * @param url
     * @param branch
     * @param userName
     * @param password
     * @return
     */
    public static Boolean clone(File file, String url, String branch, String userName, String password) {
        try {
            UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(userName, password);
            CloneCommand cloneCommand = new CloneCommand();
            cloneCommand.setURI(url)
                    //设置clone下来的分支
                    .setBranch(branch)
                    //设置下载存放路径
                    .setDirectory(file)
                    //设置权限验证
                    .setCredentialsProvider(user)
                    .call();
        } catch (Exception e) {
            log.error("GitUtil => clone error", e);
            return false;
        }
        return true;
    }

}
