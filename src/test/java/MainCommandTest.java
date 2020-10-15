
import com.voroniuk.delivery.web.command.LoginCommand;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;


public class MainCommandTest extends Mockito {
    @Test
    public void ShouldDoSomeThing() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String login = "aaa";
        String password ="aaa";


        when(request.getParameter("username")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);

        LoginCommand loginCommand = new LoginCommand();

        loginCommand.execute(request,response);



    }
}
