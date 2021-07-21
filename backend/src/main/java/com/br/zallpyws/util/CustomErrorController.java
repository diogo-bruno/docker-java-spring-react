package com.br.zallpyws.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CustomErrorController implements ErrorController {

  @Autowired private ErrorAttributes errorAttributes;
  @Autowired private ObjectMapper mapper;

  @RequestMapping("/error")
  @ResponseBody
  public Object handleError(HttpServletRequest request, HttpServletResponse response) {

    ServletWebRequest requestAttributes = new ServletWebRequest(request);

    Map<String, Object> errorAttributes_ = errorAttributes.getErrorAttributes(requestAttributes, true);

    if (isHtmlRequest(request) || (request.getHeader("output") != null && request.getHeader("output").equals("html"))
        || (!isAjax(request) && isHtmlRequest(request) && request.getContentType() == null)) {

      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

      if (statusCode == null) {
        statusCode = -1;
      }

      String titulo = "";
      String msg = "";

      if (statusCode == 400) {
        titulo = "Atenção <span>!!!</span>";
        String messages = "";
        try {

          StringBuilder erroMessage = new StringBuilder();
          if (errorAttributes_.get("messages") != null) {
            erroMessage.append(errorAttributes_.get("messages").toString());
          }
          if (errorAttributes_.get("message") != null) {
            erroMessage.append(errorAttributes_.get("message").toString());
          }

          JsonNode jNode;
          jNode = mapper.readTree(erroMessage.toString());
          for (JsonNode message : jNode) {
            if (message != null && !message.isNull())
              messages += message.textValue() + "\n";
          }
        } catch (IOException e) {
          messages = errorAttributes_.get("message").toString();
        }
        msg = messages;
      } else if (statusCode == 404) {
        titulo = "Página não mapeada<span></span>";
      } else if (statusCode == 500) {
        titulo = "Houve um erro no processamento <span>:(</span>";
      } else if (statusCode == -1) {
        titulo = "Página não pode ser acessada<span></span>";
        statusCode = 200;
      } else {
        titulo = "Opsss... <span>:(</span>";
        msg = statusCode + " - " + errorAttributes_.get("message").toString();
      }

      String contents = getHtmlErrorMsg(titulo, msg);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("text/html; charset=utf-8"));
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
      ResponseEntity<Object> response_ = new ResponseEntity<Object>(contents, headers, HttpStatus.valueOf(statusCode));
      return response_;

    } else {
      return new ErrorJson(response.getStatus(), errorAttributes_);
    }

  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

  private String getHtmlErrorMsg(String titulo, String msg) {
    String img = "data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAD6APoDASIAAhEBAxEB/8QAHQABAAEFAQEBAAAAAAAAAAAAAAkBAgYHCAUEA//EAEYQAAEDAwIDBAYGBwcCBwAAAAEAAgMEBQYHERIhMQhBUWETIjJicYEJFFKCkaEVFkJykqKxIyQzNENTYxfBJnOys9Lh8P/EABsBAQACAwEBAAAAAAAAAAAAAAAFBgMEBwIB/8QANBEAAgIBAgMFBgYCAwEAAAAAAAECAwQFEQYhMRJBUWHRInGBkaHBExQjMrHhQvAVJFKS/9oADAMBAAIRAxEAPwCVNERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAW8Q32Vysb7ZV6HxPcIiIfQiIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiAsb7ZV6sb7ZV6+s8x6BERfD0EREAREQBEVCdkBVF8lwudutVK+tuldT0dPHzfLUSCNjfi5xAWqsq7WegeKOfFU6gUlfPHyMNsY+rdv8WAt/NY7Lq6lvZJL3mvflUYy3umo+9pG4EXJeQfSG4FSOczGsEv1z25B9TJFStP5vd+SwO6fSIZxK4izadWSmb3Gpq5Zj/KGrQnq+HD/Pf3bkRbxPpdXJ27+5N/Y7vJ2Qc1HXV9vXXCoJNNR4xSg9A2gkft/FIvPf25e0C48TbpYmDwba27fm5YXruIvH5Go+MdNX/r5f2SSbjxTceKjkg7d2vMR/tH45N5PtpH/pkC923/AEhOqdPwi44ZjNWB1LDPCT/M7b8F9jrmI+ra+B6jxhpknzcl8PTc7+371VcZWT6RWhcWsyTS+qj+1JQ3Bkn8r2t/qtmY124dBb8WRV94uNjkd3XGheGg+HHHxt/NbVep4lv7Zr48v5JCjX9NyOULV8eX87HQKLHsX1AwjNYRUYnltpu7CN/7pVskcPi0HcfMLIN+fRbkZKS3T3JaE42LtQe68iqIi9HoIiIAiIgCIiAIiICxvtlXq0N9bfdXIfEtgiIh9CIiAL83yMjBc9wa1o3JPIALRes/a8030pdPZ7fMMkyGLdpoKKQejhd/zS82s82jd3kuI9U+0hqvq3LLDfr++gtLz6tqtxMNOB7+x4pfvH5BRWXq+Pi+yn2peC9Su6nxLh6c3BPtzXcvuzuTUvtgaN6cumoGXt2Q3WLdporSBKGu8Hy7+jb+JPkuXc/7d2rOTmWmxCjoMVo3khr4wKmq4f8AzHjhB+DFzaAGjhaAAPBVVdyNZyb+UX2V5epRc7inUMzdQl2I+EfXqetkmW5VmNUa3LMlud4mcfarap8u3wBOw+QC8kADoNkRRUpOb3k9yuznKx9qb3fmERF8PIREQBERAERF8BfTzz0dQ2ro55aednsywyFj2/BwO4W3MF7WOuWCOjigy996ombf3S8t+st2HcJDtIP4lqBFmqvtoe9cmvcbOPmZGI+1RNxfkzvbTnt+4LfDFQ6jWKqxupdsDVwb1NIfM7D0jB90jzXS2OZTjmXWyO9YvfKK60MvNtRSTNkZ8Dt0PkeahzXuYhnGYYBdBesLyOus9YObn00hDZPJ7D6rx5OBU3ja9bB7XrtLx7y2afxlkVNRy49teK5P0f0JhhzVVxppB296SqfDZdY7WyjkIDBeaCMmEnxmhHNnm5m48guurJfrPktsgvdgulLcaCqbxw1NNKJI3jyI5Kx42ZTlx7VUt/5L3gapi6lDt48t/LvXvR6KKgO43VVtEgEREAREQFN+eyqqBoB+KqgCoTshIC1Vrr2g8P0OsgmujxX3urYTb7TC8CWY9ON5/wBOMHq4/AblY7bYUxc7HskYcjIqxa3bdLaK6tmZ5zn2J6cY/Pk+ZXqC20EH7ch9aR3cxjRze49zRzXBOunbKzTUo1GPYQ6oxrG3EsJY/hraxn/I9v8AhtP2GnfxJ6LUmp2q2a6u5C/IszuZneCRTUse7aakZ9iJm/Lzd7R7ysQVRz9Znk+xTyj9Wcy1rim7Nbpxd4V/V+i8gNkRFCFSCIqEgDdxAHiSgKotiaf9nzWDUwRz4vhdZ9Sk6V9aPqtNt4h79i4fugrojDPo8ZHNjqNQ8/4Xci6ls8H5ell/7MW7Rp2Tkc4Qe3i+RLYeh5+dzqre3i+S+pxmrfSMJ4Q8OPgDuVJvjHY50AxprXOwv9LzN/1bpUvqD/DuGfyrZtn0/wAGx9jWWHDrLQBnT6vQRR7fMBSdfD1r/fNL6+hYKeCcmS3usjH3Jv0IjKXH8grztQ49danfmPRUUr9/wavSbp1qG4FwwHJSAOZFpqP/AIKX9kbWNDWeqB3AK7bzK2Vw7DvsfyN+PA1f+Vz+X9kOtVhuY0LeKtxC+wAdTJbJ2/1avImZJTO4KqKSFw6tlYWH81NBwn7RXw3CwWO7MMd0s9DWtPVtRTskB/iBXmXDq/xs+n9nifA0dvYu+cf7IawWn2SCqqVPJOzPoVlXE66aZ2Zkjh/i0kX1V4894i1agy76PnT64NfLheW3myynctiquGrh37hz4XgfeK07dByYLeDT+hF5HBufVzqcZ/HZ/X1OC0W9s77F+uGFiSpoLRTZNRR8/S2qTil284X7O/h4lo6so6y3VclBcaSekqoTtJBPE6ORh8C1wBCirse3He1sWit5ODk4cuzkQcfevv0PyREWE1Qs40r1n1A0cuwuOGXlzKZ7w6pts5L6SpHvM7j7zdj5rB0XuuydUlOD2Zlpvsx5qyqTUl3ok70J7TmDa0wNt0b/AND5KxnFNaaiQEv26uhfyEjfwcO8d63OHA9FDFS1dVQ1UNdQ1M1NU07xLDPFIWSRvHRzXDmD5hdvdmvtmxXx9Jger9bFT3F3DDRXt2zIqk9Gsn7mSH7fsu79j1tenazG/aq/lLx7mdG0LiqOW1j5uyn3PufozsBFaHbnqq7gqfLqVREQBU32VVZI5rGF7jsGjc/BAaU7SXaQs2iFlbQUDYrhldyiLqCicfUiZ09PNtzDAeg6uPIdCRG7kuTX/Mb7WZNlF0muNzr3+knqJjzPgAOjWgcg0cgvX1Uza4ai6i5BmNylc99fXSehBO4jgY4tiYPABgHz38ViqompahPNsa6RXRfc49r2s26pe477VxfJfd+f8BERRpABUJDRuTsAvdwzCcp1CyCnxjDrNPc7jUdI4x6sbe98jjyY0faK7z0J7GOHadimyPPBT5Jkbdnta9u9FRv/AONh9tw+24fABb+Fp12a/Z5R8f8AepMaVomTq0v0ltHvk+n9s5Z0f7J2qGrIgur6L9XrBLs79I3CMh0rPGGHk5/xPC3zK7R0t7JukOmIirGWQX67sAJuF1aJnB3jHH7EfyG/mtztbwjYK5WvE0rHxVulu/FnSdN4cwtOSko9qfi/su4tawNAAAAHQeCrt3qqKTJ8IiIAiIgCIiAKmw8FVEBQgHqFh2f6Taean0Ros2xahuXLZk7o+Goi82St2e35FZki8yjGa2kt0eLKoXRcLFun3M4R1b7BN/swmu+kl2deaZu7jaq57WVLR4Ry8myfB3CfMrlS62q62K4z2e+W2qt9fSu4J6apiMcsZ82nmpmHDcbLX2q+hunusdrNDl1naaqNhFLcqfaOrpz7r+8e67dvkoHM0Kuz2sfk/Du/opuqcH03J2YXsy8O5+hE8i27rn2ac60Rqn1tW03fG3v4YLvBHs1m55Nnb/pu8+bT3HuWolVrqZ0T7Fi2ZzzJxbsOx03x7MkEIBBBG4PcURYzXOtOyt2tqjHpqLTTVG5GW1SFtPa7vO4l9I48mwzOPWPuDzzb0PLmO7Izu3iDgd+e4ULxAcCCNwRsQVJr2O85uGc6G2ie7TunrLPLLaZZXndz2wkejJ8+BzB8la9E1Cd3/Xs57Lk/sdH4S1m3J3wb+bit0+/Zdz+xu9ERWIvIX5yNa+Mxno4bH4L9FQjdAQ/aiYnX4Lnl/wARuURZPbK+aLmPbj4iY3jxDmFp381jykn7SnZctGtkMeQWSrhtOV0kXoo6l7d4auMdIptufL9l43I6bEdOLL72WtfrBWuoptNLlW8J5TW9zKiJ3mC07/iAVRs3S78ex9iLce5o5Dq3D+XhZEvw4OUG+TS3+D27zVa2HotofmGt2RG0Y9F9Wt1KWm43SVhMNKw93vyEdGD4nYc1sTTPsSas5hcIZczo24nZw4OnkqHskq3t72xxNJ2Pm8jbwPRd84FgWL6b4zSYliNsZRW6jHqt6vkefake7q57j1JWxp+jzul271tHw72buicL3Zc/xcyLjWu58m/RHkaT6PYVo9jbLBiNuEbngOq62UB1RVyD9uR39GjZo7gs52Cqit8IRrj2YLZHTaqoUQVda2S6JBERejIEVCdliuZ6o6fae0/1jNMxtdoG24jnnAld+7GN3O+QXmUowW8nsjxZZCqPam9l5mVqnRcv5b2/tKbOXw4tZL3kMjTsHiMUsJ+9J6233Vqe/fSFaj1Zc3HsIsNujPsuqZZal4/DgH5KOs1fDq5Oe/u5kJkcS6Zj8nZv7k39en1O+E3CjSru2x2h60kxZPbaIH9mntUXL+PiK8p3a67RDnF3/UecbnoKKm2/9taz1/GXRP5f2R0uNMBdIyfwXqShb96b7qMek7ZPaKpCP/HUU4H+/bKd2/4MCymzdvnWm3kC62zG7qzv46WSBx+bH7fkvUNexZdd18DJXxlp0/3KS+Ho2SIIuO8X+kQsc5ZFmendfR9zp7bVMqGjz4Hhh/MrduEdp7RLP3x0tmzmjpq2TpSXHekl38AJNg4/Alb1OoY1/KE1v8v5JfG1rAzHtVat/B8n8nsbXRWMkbI0SMcC0jcEHcFXbjxW4ShVEVC4DqgPKyWaw01guM+Uil/Q8VNI+u+tNDovQBpL+MHkRtvyUReZV2N3PLLvcMPs7rVY6irkfb6Nzy4wwb+qNz4jnt3b7dy627dmuG3BorjdXuXhlTfpGO6N9qKm+fJ7h4cI7yuMFUNcy43WKmP+PV+ZzHi/UoZN6xa0n2Or8/D3Lv8AMIiKBKaCduZUlfYsw+uxLQm2SXGB0M98qZrtwPbs4RybCPcebGNPzXGnZg0Ul1m1Ehp7hA847ZSysuz9jtIN/wCzp9/F5HP3Q7yUnsEUUEbYYImsYxoa1rRsGtHIADuGys2g4kk3ky6dF9y/8GabJOWfPpttHz8X9j9kRFZzoIRU37lVAU2Hgmw8FVEBTYeCqiIAiLyMmyiwYdZanIsnu1NbbdSM45qiofwtaP8AuT3AbknkF8bSW7PMpKCcpPZI9YkDqtR6u9pvS7R8SUN3urrlemgltpt5bJOD3ekO/DEP3iD5Fct67dtrJcxdUY3pS6osNldvG+5EcNdVDvLP9lp8vXPi3ouXnufLI6WV7nySOLnve4uc5x6kk8yfNV7N12MN4Y3N+Pd8CkarxhCpurBXaf8A6fT4LvN+am9tHV/PHTUVgq2YlankgQ2871Lm+9UEcQ5fYDVoaqqKiuqX11dUzVNTId3zTyOkkcfEucSSvzRVq7JtyHvbLcoeXnZOdLt5E3J/70XRBERYTUCIiAIiIAqOa142c0EeaqiA2Bp1r3qzpdLGMUy+rFEw87fVuNRSuHhwP9n4tLSuutJu3dhWTuhtGptB+rFweQ0VrCZaGQ+Z9qL72495cCot/F1LIxXtGW68HzJrTtfztNaVc94+D5r+vgTNUNfRXKkirrfVw1NNO0PimhkD2SNPQhw5ELA9dtWrdo1p5X5bVlktYR9XttMT/mKpwPA390c3O8mlR16P9oHUXReuaccuRq7Q929RZ6txdTPHeWd8Tveb8wV9vaJ15uGu2U0lybST26zWynEVDQSSB5ZI4AyyOI5FxPIH7LR4lTtmu1yx3KC2n4ffct13GNFmFKVa7NvRL39+/gjWd2utyv11rL5eax9VX3Cd9TUzvO7pJHndx/8A3cvkRFVG23uznEpObcpdWF9NstlwvVypLNaaR9VXV87KamgjG7pJXuAa0fMr5l2V2EtD/TSya1ZJSeozjprDHI3qebZan+rGn98+C2cLFlmXKqPx8kSGladPVMqOPDp3vwXe/TzOkNA9I7fozp5Q4rAGS3B4+tXSqb/r1TgOIj3W+y3yaPNbJVANlVdArrjVBQh0R2qimGNXGqtbRS2QREXsylo6lXK0H1irkAREQBEVkj2saXvIAaCST3BAYhqjqliWkeLT5Vl9eIoGbsggYQZqqXblHG39px/ADmdgo1da9d8z1vvv1+/TGktNM8/o+0xPJhpx9p3+5Jt1cfgNgr+0Hq3dNYNSble56p5tNDNJR2en39SKma7bj2+08jiJ68wOgWtVS9U1OWVJ1V8oL6nKeIeILNQsePS9qk//AK835eCCIihiqhE6DcrZGl3Z61U1dcyfFsedDbHHY3SuJhpR48LiN5PgwH5L3VVO6XZrW78jNRj25M/w6YuT8EjW6pxN4g3iG55Ad5Xe2AfR/wCB2iOKr1CyCuyGqGxfTUxNJS7+HImRw+8Pgt+Ypo/pfhETIsVwGyW8t/1GUbHS/ORwLj+KmaNBvnzsaj9X/vxLVicGZly7V8lBfN/Tl9SKi2YRm17AdZcMv1eD0NPbppAfmG7L32aFa1SsEkelGVOae/8ARkg/qFLO1gaNgNgOgHJXbLejw9Xt7U38kS8OB6EvaufyX9kQtw0s1OtTS+5acZPTNb1c+0z7D5huyxqohmo5DDWQS08gOxZLGWO/A81M6WleVesTxnJYXU2RY7bbnE4bFtXSMlH8wK8z4dj/AIWfNGK3geO36Vz3816Mh0RSSZv2JtD8sbJNarRU41WO3Iltc5bHv5wv4mbeQAXMep3Yh1Wwhs9xxV0OXW2Pd390Z6Osa3zhJPF9xxPkovJ0fJx+e3aXl6FfzuF9Qwk5qPbj4x5/Tqc7ov0nhnpZ5KWqglhnhcWSRSMLHscOoLTzB8ivzUWV5prkwiIh8CIqgOe4Rxsc97iGta0blxPIADvO6DryM80Q0ouesuodvw+jEkdHv9ZudS0f5ekaRxn953st83eRUq1ks1tx+0Udjs1HHS0NvgZT08LBs2ONo2aB8gtQ9lTRFmj2n0b7vTNGSX0Mq7o/bnDy/s6cHwYDz8XF3kt3K8aTg/lKd5ful19DrvDWk/8AG4vbsX6k+b8l3L/e8IiKVLGEREBaANzyVytHUq5AEREAXxXmnlrLRXUcD+GSemkiYfBzmkA/iV9qoRuF8a35HxrdbEME9LPQ1EtDVRmOemkdBKxw2LXtJa4H5hWLpntt6JOwfMv+pVipiLJk05+thrfVpq/bd3wbIAXD3g7xC5mXO8rHljWyql3HDdRwrNPyZ49nc/mu5hPkiLXNI7a7KvZV05vOL2nVLLa2nyiWub6emoQP7nSOB2LZWnnJI0jYh3qg9x6rsOCnhpoWQQRMjjjaGsYxoa1oHQADoFHx2KdcP1DzE6dZDWcNiyWYCle93q0teRs0+TZBs0+8G+akLaVeNIlRPGTpWz7/AHnXeGLMSzBi8aKi1yl47rxfn1RUDYbKqIpUsYREQBERAFbwjkd1ciA1VrH2ctN9ZqR8l8tgob01u0F4omhlQw9wf3SN912/lt1Ue+s2g+d6JXcUmS0oqbZUvLaK7UzSaef3T/tybfsn5bhSubbrycmxiwZhY6vG8mtVPcbbWsLJ6ednE1w/qCOoI5g9FF5+l1Zi7S5S8fUruscO42qRc4rs2ePj7/8AdyHNFvbtDdljKtIrpPecaoq29YhKeOKqYwyS0Q3/AMOcNG+w7pNtiOuxWh/SxEcpG/iqZfRZjTddi2ZyvMwr8C103x2a+vuLl052I9D/ANd8tOpuRUfFZMdm2oWPb6tTXgbg+bYhs794t8CtS6QaG53rPeoqDG7bLBbA8Csu80ZFNTM357OPJ79ujW78+uw5qT/A8KsenWJWzDMcp/RUFrgEMe/Nzz1c9x73Odu4nxKl9G053Wfj2L2V082WbhbRZZN6y74+xHpv3v0RkIGwVURXA6gEREAREQFo69FciIAiIgCIiAxrULBbJqRh10wrIYBJRXOAxOdt60T+rJG+81wDh8FFDn2D3vTjMLphWQxcNbapjE54GzZmdWSt91zdiP8A6UwR6LmHts6HfrziTdSMdo+O+Y1CTUsjb61VQdXjl1dGd3jy4h3hQus4P5mr8SC9qP8ABVOKtI/P4/5ipe3D6rvX3RH2ioCCA5p3B57qqphyoDcEFri0g7hwOxB8Qe4qS/sna4N1d0+ZR3mrDslx8MpLkHH1p2bf2dR94DY+813ko0FnGjGqd00d1Bt2ZW/jkp4z6C40rT/maRxHGz4j2m+80KR0zNeHcm/2vr6k7w/qz0rKUpfslyl6/Alq6oTstaZV2itHsMxyhyW9ZpR/V7nTMq6KGAmapqI3DdpbE3d3zOw371znnH0htQ58lNpzgbQwEhtXeJjufMQxnl83q336hjY6/Ul9zp2XreBgr9Wxb+C5v6Ha+4Tib4qMi99sntCXpzuDMobaxx5R0FBFHw/ecHO/NY2/tIa9vcXO1ZyHc+E7QPwDVHS4gx10i38vUg58a4MXtGEn8vUlbBBQEdyi1tfat7QlpkEkWpldUbfs1cEMzf5mbrZeI/SA6mWuRkWYYzZr5Bv676cOo5tvlxMP8IWSvXsWb2luvh6GWjjHT7XtNSj716NkgCLRGmnbI0c1Dlht1TdZcbukuwFLdgI2Oce5kwPAfmQT4LeUcrJWh8bmua4cTSDuCPEeKlarq749qt7osmNl0ZkO3jzUl5H6qm26qiymwWvaHNIIBB5ELGKnS7TWsqjX1mnuNz1JO5mktcDnk+O5buspReZRjL9yPE64WfvSZ89LR0tDAymo6aKnhjGzI4mBjGjyA5BfQiL0uR6S25IIiIfQiIgCIiAIiIAiIgCIiAKyRjXscyRoc1w2cCNwQr0QEY/at0Sfo/qFJU2mlLMayAvqraWg8NPJvvLT/dJ3b7pHgVpRSya36VWrWPT64YdcOCKoePT2+qI501U0H0b/AIfsuHe1xUVF6s10xy812P3ukdS3C3VD6aphd1ZI07EeY8D3jYqk6vg/lbe3H9sv5OS8T6T/AMdk/i1r9OfNeT716HxIiKIKyNh17+iIiAIiIAiIgKFocC1wBB8e9bg0X7T+pGjdRDRQVr71jocBLaa2UlrG9/oJDuYj5c2+S1Aiy032US7db2ZsY2Xdh2K2iTjLyJZ9JtY8J1kx4X3ELjxPi2bWUU3q1NJIR7Mjf6OG4PcVne4PRQ/4BqBlWmWT0uXYfcXUldTcnNJ3iqI/2opW/tMPh3dRsRupTdI9SbTq1gNqzi0M9E2ujLainJ3NPUNPDJEfg7fn3jY96uWmams2PYnymvr5nVOH9fjq8HXYtrI9fBrxXoZoiIpYsgREQBERAEREAREQBERAEREAREQBERAUI3Gy4z7dmh/1inj1oxyi/tqZrKa/MjHtRdI6gj3fZcfAtPcuzV8d0ttDd7fU2u50sdTSVkL4J4ZBu2SNw2c0jwIJC1svGjl1OqXf9GR+qafXqeNLHs7+j8H3MhoRbM7QGi120UzqpsssEz7FWvdNZq13Ns0G/wDhl3+4zoR122PQrWa5/bVOmbrmtmjiuTj2YlsqbVtJBFfDBPUzRU1NBJNNM8RxRRMLnyPJ2DWtHMknuC7W0C7D9uhpKbK9aoDU1UoEsFgDtooe8fWHD23e4DwjoeLoM+JhW5k+zWve+5G5pulZGqWfh0Lp1b6L3nH2NYbl2Zz/AFbEsXut5kHIiipHyhvxcBsPmVsKn7KHaGqoROzTKtYHfsy1VOx38JfupO7VZ7XY6GK2Wa20tBSQjaOnpomxRsHgGt2AX28IVhr4eqS9ubb8i708EY6j+ta2/LZL7kSeTaJav4bE+pyXTi/UlOz2p20xliHnxx8QAWEgh3TnspothtstK6ydlTTLVimnrWW2Kw5A5pMd0oIgwuf3emjGzZR8dneBWDI4faW9Et/J+pp53BMoxcsOzd+EvVfdEY6LLdT9Lcv0hyiXFMxoRFMBx01THuYKyLflJE7vHiOoPIrElXJwlXJwmtmijW02UzdVi2kuqC7x+jyq6yXTzKKOUuNLT3prod+gc+FvHt+DVwtbrdcLvcKa02iimrK6skbDTU8LC6SV5Owa0DqVKP2cNJ5NHdLbdi9eWOutQ51dc3MO7frEm27Ae8MaGt393fvUzoVU5ZH4i6JFq4OxrLM53pezFPd+/uNqIiK5HUgiIgCIiAIiIAiIgCKgJ36fNVQBERAEREAREQBERAeFl2FYvnlmmx7MLHSXW3zc3QVDOIBw6OaerXDuI2IXP91+j/0dras1FtvWS22Jx3+rxVUcjR5AyMLvzK6dRa92JRkPe2KbNLK07EzWnkVqTXijU2l3Zj0j0mq23XHbC6quzB6lxuMn1iePx4NwGx/dAPmtsbc91VFkqqhTHs1rZGejGpxYfh0xUV4LkERFkMwVNt1VEBjOdadYXqVZ3WHNsfpbrRk8TGyt9aJ/2mPHrMd5grQtb9H5pBUVZnpMgyikhJ39AypieB5Bzoy7811Ai17sSjIe9sE2aOTpmHmNSyK1J+LRrPS3s76V6QPNZiWPg3JzSx9yrHmeqLT1AefYB8GgLZYGyqiy11wqj2YLZGzTRVjQVdMVFLuQREXsyhERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQH//2Q==";
    return "<!doctype html><html lang='en'><head> <meta charset='utf-8'> <title>Zallpy WS</title> <style>html{padding: 30px 10px; font-size: 20px; line-height: 1.4; color: #737373; background: #f0f0f0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;}html, input{font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;}body{max-width: 500px; _width: 500px; padding: 30px 20px 50px; border: 1px solid #b3b3b3; border-radius: 4px; margin: 0 auto; box-shadow: 0 1px 10px #a7a7a7, inset 0 1px 0 #fff; background: #fcfcfc;}h1, h3{margin: 0 10px; text-align: center;}h1 span, h3 span{color: #bbb;}p{margin: 1em 0;}ul{padding: 0 0 0 40px; margin: 1em 0;}.container{max-width: 380px; _width: 380px; margin: 0 auto;}#goog-fixurl ul{list-style: none; padding: 0; margin: 0;}#goog-fixurl form{margin: 0;}.img-center{text-align: center;}.img-center img{width: 90px;}</style></head><body><div class='container'> <div class='img-center'> <img src='"
        + img + "'/> </div><h3>" + titulo + "</h3> <p></p><p style='text-align:center'>" + msg + "</p></div></body></html>";
  }

  private boolean isAjax(HttpServletRequest request) {
    String requestedWithHeader = request.getHeader("X-Requested-With");
    return "XMLHttpRequest".equals(requestedWithHeader);
  }

  private boolean isHtmlRequest(HttpServletRequest request) {
    String acceptHeader = request.getHeader("Accept");
    List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
    return acceptedMediaTypes.contains(MediaType.TEXT_HTML);
  }

  public class ErrorJson {

    public Integer status;
    public String error;
    public String message;
    public String msg;
    public List<String> messages;
    public String timeStamp;
    public String trace;

    @SuppressWarnings("unchecked")
    public ErrorJson(int status, Map<String, Object> errorAttributes) {
      this.status = status;
      this.error = (String) errorAttributes.get("error");
      this.message = (String) errorAttributes.get("message");
      if (errorAttributes.get("messages") != null) {
        try {
          this.messages = (List<String>) errorAttributes.get("messages");
          this.msg = StringUtils.join((List<String>) errorAttributes.get("messages"), "\n");
        } catch (Exception e) {

        }
      }
      this.timeStamp = errorAttributes.get("timestamp").toString();
      this.trace = (String) errorAttributes.get("trace");
    }

  }

}
