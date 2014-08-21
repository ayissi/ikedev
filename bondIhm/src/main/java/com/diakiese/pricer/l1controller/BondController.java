package com.diakiese.pricer.l1controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.diakiese.pricer.l2form.Bond;

@Controller
public class BondController{
	Logger log = Logger.getLogger(BondController.class); 
	
//	@RequestMapping("/")
//	public ModelAndView index() {
//		return new ModelAndView("bondParameter"); 
//	}


		
//	@RequestMapping(value="bondParameter", method=RequestMethod.GET)
	@RequestMapping("/")
	public String loadFormPage(Model m){  
		m.addAttribute("bond", new Bond());
		m.addAttribute("message1", "no config:");
		return "bondParameter";
	}

	@RequestMapping(value="bondParameter", method=RequestMethod.POST)
	public ModelAndView submitForm(@ModelAttribute Bond bond, Model m) {
		m.addAttribute("message2", "Successfully saved person: " + bond.toString());
		log.info("Successfully saved person: " + bond.toString());  
		return new ModelAndView("bondParameter");    
	}

}
