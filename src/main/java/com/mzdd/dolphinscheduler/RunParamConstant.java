package com.mzdd.dolphinscheduler;

/**
 * 运行参数常量
 * @author mzdd
 * @create 2023-03-21 21:26
 */
public interface RunParamConstant {

    /**
     * git文件存放的本地路径
     */
     String LOCAL_PATCH = "E://test//5";

    /**
     * git 仓库地址
     */
     String GIT_URL = "http://git.xxx.com/projects/DataSql.git";

    /**
     * git 分支
     */
     String GIT_BRANCH = "dev";

    /**
     * git 账号
     */
     String GIT_USER_NAME = "mzdd";

    /**
     * git 密码
     */
     String GIT_PASSWORD = "xxxxxxxx";

    /**
     * dolphinscheduler 中工作流、任务的前缀
     */
    String NAME_PREFIX = "mzdd";

    /**
     * dolphinscheduler 地址
     */
    String DS_DOMAIN = "http://1.2.3.4:12345";

    /**
     * dolphinscheduler 项目id
     */
    String DS_PROJECT_ID = "6666666666666";

    /**
     * dolphinscheduler 环境code
     */
    Long DS_ENVIRONMENT_CODE = 6666666666666L;

    /**
     * dolphinscheduler 数据源id
     */
    Integer DS_SOURCE_ID = 66;

    /**
     * dolphinscheduler token
     */
    String DS_TOKEN = "xxxxxxxxxxxxxxxxxx";

    /**
     * dolphinscheduler 全局参数
     */
    String DS_GLOBAL_PARAMS = "[{\"prop\":\"begin_date\",\"value\":\"$[yyyy-MM-dd-1] \",\"direct\":\"IN\",\"type\":\"VARCHAR\"},{\"prop\":\"end_date\",\"value\":\"$[yyyy-MM-dd-1] \",\"direct\":\"IN\",\"type\":\"VARCHAR\"},{\"prop\":\"etl_batch_id\",\"value\":\"$[yyyyMMddHHmmss]\",\"direct\":\"IN\",\"type\":\"VARCHAR\"},{\"prop\":\"run_type\",\"value\":\"INC\",\"direct\":\"IN\",\"type\":\"VARCHAR\"}]";

}
