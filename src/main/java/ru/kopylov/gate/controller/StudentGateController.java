package ru.kopylov.gate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.kopylov.gate.api.StudentGateApi;
import ru.kopylov.gate.client.api.StudentDataApi;
import ru.kopylov.gate.client.model.StudentDataCreateRequest;
import ru.kopylov.gate.client.model.StudentDataResponse;
import ru.kopylov.gate.model.StudentGateCreateRequest;
import ru.kopylov.gate.model.StudentGateResponse;

@RestController
@RequiredArgsConstructor
public class StudentGateController implements StudentGateApi {

    private final StudentDataApi studentsFeignClient;

    /**
     * Проксирует создание студента во внутренний DATA-SERVICE.
     * Получает запрос от клиента, преобразует модель и перенаправляет.
     */
    @Override
    public ResponseEntity<StudentGateResponse> createStudent(StudentGateCreateRequest request) {
        StudentDataCreateRequest dataRequest = new StudentDataCreateRequest();
        dataRequest.setFullName(request.getFullName());
        dataRequest.setPassport(request.getPassport());

        StudentDataResponse dataResponse = studentsFeignClient.createStudentDataInData(dataRequest);

        StudentGateResponse gateResponse = new StudentGateResponse();
        gateResponse.setId(dataResponse.getId());
        gateResponse.setFullName(dataResponse.getFullName());
        gateResponse.setPassport(dataResponse.getPassport());

        return ResponseEntity.status(201).body(gateResponse);
    }

    @Override
    public ResponseEntity<StudentGateResponse> getStudentById(Long id) {
        StudentDataResponse data = studentsFeignClient.getStudentDataByIdFromData(id);

        StudentGateResponse response = new StudentGateResponse();
        response.setId(data.getId());
        response.setFullName(data.getFullName());
        response.setPassport(data.getPassport());

        return ResponseEntity.ok(response);
    }
}

