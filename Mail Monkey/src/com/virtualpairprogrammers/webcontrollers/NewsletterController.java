package com.virtualpairprogrammers.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewsletterController 
{	
	@RequestMapping("/build-newsletter")
	public ModelAndView displayAllCustomersOnWebPage()
	{
		return new ModelAndView("/newsletter.jsp");
	}
}
