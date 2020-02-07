package br.com.async.web.controller;

import br.com.async.model.Employee;
import br.com.async.repository.EmployeeRepository;
import br.com.async.web.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.ForkJoinPool;


@RestController
@RequestMapping(value = "/employee-management", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class EmployeeRESTController {
    @Autowired
    private EmployeeRepository repository;

    public EmployeeRepository getRepository() {
        return repository;
    }

    public void setRepository(EmployeeRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/async-deferredresult", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public DeferredResult<ResponseEntity<?>> handleReqDefResult() {
        System.out.println("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
        output.setResult(ResponseEntity.accepted().build());


        System.out.println("servlet thread freed");
        System.out.println(output.getResult());

        return output;
    }

    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    Employee getEmployeeById(@PathVariable
                             @Min(value = 1, message = "id must be greater than or equal to 1")
                             @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee id '" + id + "' does no exist"));
    }

    @PutMapping("/employees/{id}")
    Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id).map(employee -> {
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getLastName());
            employee.setEmail(newEmployee.getEmail());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}