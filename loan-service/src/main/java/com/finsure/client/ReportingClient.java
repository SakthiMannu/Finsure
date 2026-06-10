package com.finsure.client;

import com.finsure.dto.NotificationRequestDTO;
import com.finsure.dto.TaskRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "REPORTING-SERVICE", configuration = FeignClientConfig.class)
public interface ReportingClient {

    @PostMapping("/api/notifications/internal")
    void createNotification(@RequestBody NotificationRequestDTO dto);


    @PostMapping("/api/tasks/internal")
    void createTask(@RequestBody TaskRequestDTO dto);
}
