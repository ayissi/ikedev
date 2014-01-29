/**
 * 
 */
package com.diakiese.pricer.l1weblayer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author guy belomo
 *
 */
@Controller
public class HomePagesController {
	@RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("obligation");
    }
    		
}
