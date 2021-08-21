package com.template.system.controller;

import com.template.common.service.Common;
import com.template.job.service.Job;
import com.template.log.service.Log;
import com.template.test.service.Test;

/**
 * @ClassName: SystemController
 * @Description: TODO
 * @Author: LiuJiaTao
 * @CreateDate: 2021-08-21
 * @Version : V1.0.0
 */
public class SystemController {

    public static void main(String[] args) {
        Common.sayHello();
        Test.sayHello();
        Job.sayHello();
        Log.sayHello();
    }
}
