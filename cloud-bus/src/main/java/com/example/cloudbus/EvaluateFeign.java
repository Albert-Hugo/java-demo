package com.example.cloudbus;

import com.oppein.bsp.behaviorcenter.api.evaluation.EvaluateResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * @author Ido
 * @date 2020/10/28 14:06
 */
@FeignClient(name = "test",url = "localhost:8073")
@Service
public interface EvaluateFeign  extends EvaluateResource {

}
