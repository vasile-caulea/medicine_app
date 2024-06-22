package com.pos.medicineConsults.services;

import com.pos.medicineConsults.exceptions.ForbiddenException;
import com.pos.medicineConsults.exceptions.NotFoundException;
import com.pos.medicineConsults.exceptions.UnauthorizedException;
import com.pos.medicineConsults.interfaces.services.IRequestService;
import com.pos.medicineConsults.utils.UserType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static com.pos.medicineConsults.utils.Utils.getFormattedDate;

@Service
public class RequestService implements IRequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAppointmentForUserType(UserType userType, String authorizationHeader, String patientId, Integer doctorId, Date date) {

        ResponseEntity<String> response;
        try {
            String formattedDate = getFormattedDate(date);
            String url;
            if (userType == UserType.PATIENT) {
                url = "http://localhost:8080/api/medical_office/patients/" + patientId + "/physicians/" + doctorId + "/" + formattedDate;
            } else {
                url = "http://localhost:8080/api/medical_office/physicians/" + doctorId + "/patients/" + patientId + "/" + formattedDate;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Could not find the appointment");
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ForbiddenException();
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException();
            }
            throw new InternalError("Could not process the request");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InternalError("Could not process the request");
        }
        return response.getBody();
    }
}
