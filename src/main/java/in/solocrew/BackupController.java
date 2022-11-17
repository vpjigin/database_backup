package in.solocrew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backup")
public class BackupController {


    @Autowired
    BackupService backupService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> backup(){

        String header = backupService.backup();

        return new ResponseEntity<>(header, HttpStatus.OK);
    }


}
