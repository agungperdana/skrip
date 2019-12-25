/**
 * 
 */
package com.kratonsolution.skrip.leon.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author agung
 *
 */
@Controller
public class DiagnosaController {
	
	@GetMapping("/prepare-diagnosa")
	public String prepare() {
		return "diagnosa";
	}
}
