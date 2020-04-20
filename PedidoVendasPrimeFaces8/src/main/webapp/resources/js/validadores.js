PrimeFaces.locales['pt_BR'] = {
		messages : {
			'org.hibernate.validator.constraints.NotBlank.message' : '{0} : não pode estar em branco',
			'javax.validation.constraints.NotNull.message' : '{0} : não pode ser nulo'
		}
}

PrimeFaces.validator.NotNull = {

		MESSAGE_ID : "javax.validation.constraints.NotNull.message",

		validate : function(element, d) {
			var label = element.data('p-label');
			if (d === null || d === undefined) {
				var b = PrimeFaces.util.ValidationContext, a = element.data("p-notnull-msg"), e = (a) ? {
					summary : a,
					detail : a
				} : b.getMessage(this.MESSAGE_ID, label);
				throw e
			}
		}
	};


PrimeFaces.validator.NotBlank = {
		
		MESSAGE_ID : 'org.hibernate.validator.constraints.NotBlank.message',
			
		validate : function(element, value) {
			if (value === null || value === undefined || value.trim() === '') {
				var msg = element.data('msg-notblank');
				var label = element.data('p-label');
				var context = PrimeFaces.util.ValidationContext;
				var msgObj;
				
				if (!msg) {
					msgObj = context.getMessage(this.MESSAGE_ID, label);
				} else {
					var msgObj = {
						summary : msg,
						detail : msg
					}
				}
				
				throw msgObj;
			}
		}
			
	};

PrimeFaces.validator.SKU = {
		
		pattern : /^([a-zA-Z]{3}\d{4,18})?$/,
			
		validate : function(element, value) {
			if (!this.pattern.test(value)) {
				var msg = element.data('msg-sku');
				
				if (!msg) {
					msg = 'SKU não é válido(JS).';
				}
				
				var msgObj = {
					summary : msg,
					detail : msg
				}
				
				throw msgObj;
			}
		}
			
}


PrimeFaces.converter['com.rlsp.Categoria'] = {
		
		convert : function(element, value) {
			if (value === null || value === '') {
				return null;
			}
			
			return parseInt(value);
		}
			
};


PrimeFaces.converter['com.rlsp.Produto'] = {
		
		convert : function(element, value) {
			if (value === null || value === '') {
				return null;
			}
			
			return parseInt(value);
		}
			
};


PrimeFaces.converter['javax.faces.Number']  = {
	CURRENCY_ID : "javax.faces.converter.NumberConverter.CURRENCY",
	NUMBER_ID : "javax.faces.converter.NumberConverter.NUMBER",
	PATTERN_ID : "javax.faces.converter.NumberConverter.PATTERN",
	PERCENT_ID : "javax.faces.converter.NumberConverter.PERCENT",
	REGEX : /^[-+]?\d+(\.\d+)?(\,\d+)?[d]?$/,
	convert : function(d, e) {
		if (e === null) {
			return null
		}
		if ($.trim(e).length === 0) {
			return null
		}
		var g = PrimeFaces.util.ValidationContext, k = g
				.getLocaleSettings(), j = d.data("p-notype"), l = d
				.data("p-maxint"), i = d.data("p-minfrac"), c = d
				.data("p-intonly");
		if (j === "currency") {
			var f = d.data("p-curs");
			if (f) {
				if (e.indexOf(f) === -1) {
					throw g.getMessage(this.CURRENCY_ID, e, f + "100",
							g.getLabel(d))
				} else {
					e = e.substring(f.length)
				}
			}
		} else {
			if (j === "percent") {
				if (e.lastIndexOf("%") !== (e.length - 1)) {
					throw g.getMessage(this.PERCENT_ID, e, "50%", g
							.getLabel(d))
				} else {
					e = e.replace(/%/g, "")
				}
			}
		}
		
		if (!this.REGEX.test(e)) {
			throw g.getMessage(this.NUMBER_ID, e, 50, g.getLabel(d))
		}
		var h = e.split(k.decimalSeparator), b = h[0].replace(
				new RegExp(k.groupingSeparator, "g"), ""), a = h[1];
		if (l && b.length > l) {
			b = b.substring(b.length - l)
		}
		if (a && i && a.length > i) {
			a = a.substring(0, i)
		}
		if (c) {
			return parseInt(b)
		} else {
			return parseInt(b) + parseFloat("." + a)
		}
	}
}

