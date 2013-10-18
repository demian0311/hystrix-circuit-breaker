package com.neidetcher.hcbp.util

import com.netflix.hystrix.HystrixCommandProperties

class HystrixConfigurationUtility {
    static HystrixCommandProperties.Setter createHystrixCommandPropertiesSetter(){
        HystrixCommandProperties.invokeMethod("Setter", null)
    }
}
