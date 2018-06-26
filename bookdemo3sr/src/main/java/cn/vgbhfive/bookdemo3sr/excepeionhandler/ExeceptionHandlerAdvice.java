package cn.vgbhfive.bookdemo3sr.excepeionhandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @time:
 * @author: Vgbh
 */
@ControllerAdvice
public class ExeceptionHandlerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exeception (Exception exception, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMsg", exception.getMessage());

        return modelAndView;
    }

    @ModelAttribute
    public void addAtributes (Model model) {
        model.addAttribute("msg", "额外信息");
    }

    @InitBinder
    public void initBinder (WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    /*
           @ControllerAdvice声明一个控制器建言，@ControllerAdvice组合了@Component注释，所以自动注册为Spring的Bean。
           @exceptionhandler在此定义全局的处理，通过@ExceptionHandler的value属性可过滤拦截的条件，在此我们可以看见会拦截所有的Exception。
           使用@ModelAttribute注解将键值对添加到全局，所有注释的@RequestMapping的方法都可得到这个键值对。
           通过@InitBinder注解定制WebDataBinder

           通过@ControllerAdvice，我们可以实现对于控制器的全局配置放置在同一个位置，
           注解了@Controller的类的方法可以使用@ExceptionHandler、@InitBinder、@ModelAttribute注解到方法上，
           这对于所有的@RequestMapping的控制器都是有用的。

           @InitBinder：用来设置WebBinder，WebBinder用来自动绑定前台请求参数到Model中。
           @ModelAttribute：@ModelAttribute本来的作用是绑定键值对到Model中，
           此处是让全局的@Requestmapping都能得到此处设置的键值对。


     */


}
