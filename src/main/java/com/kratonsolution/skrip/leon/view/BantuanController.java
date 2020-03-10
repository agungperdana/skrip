/**
 * 
 */
package com.kratonsolution.skrip.leon.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Acer
 *
 */
@Controller
public class BantuanController {

	@GetMapping("/prepare-bantuan")
	public String show() {
		return "bantuan";
	}
}
