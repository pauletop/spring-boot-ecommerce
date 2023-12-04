$(document).ready(function() {
	"use strict"

	// Mobile Nav toggle
	$('.menu-toggle > a').on('click', function (e) {
		e.preventDefault();
		$('#responsive-nav').toggleClass('active');
	})

	// Fix cart dropdown from closing
	$('.cart-dropdown').on('click', function (e) {
		e.stopPropagation();
	});

	/////////////////////////////////////////

	// Products Slick
	$('.products-slick').each(function() {
		var $this = $(this),
				$nav = $this.attr('data-nav');

		$this.slick({
			slidesToShow: 4,
			slidesToScroll: 1,
			autoplay: true,
			infinite: true,
			speed: 300,
			dots: false,
			arrows: true,
			appendArrows: $nav ? $nav : false,
			responsive: [{
	        breakpoint: 991,
	        settings: {
	          slidesToShow: 2,
	          slidesToScroll: 1,
	        }
	      },
	      {
	        breakpoint: 480,
	        settings: {
	          slidesToShow: 1,
	          slidesToScroll: 1,
	        }
	      },
	    ]
		});
	});

	// Products Widget Slick
	$('.products-widget-slick').each(function() {
		var $this = $(this),
				$nav = $this.attr('data-nav');

		$this.slick({
			infinite: true,
			autoplay: true,
			speed: 300,
			dots: false,
			arrows: true,
			appendArrows: $nav ? $nav : false,
		});
	});

	/////////////////////////////////////////

	// Product Main img Slick
	$('#product-main-img').slick({
    infinite: true,
    speed: 300,
    dots: false,
    arrows: true,
    fade: true,
    asNavFor: '#product-imgs',
  });

	// Product imgs Slick
  $('#product-imgs').slick({
    slidesToShow: 3,
    slidesToScroll: 1,
    arrows: true,
    centerMode: true,
    focusOnSelect: true,
		centerPadding: 0,
		vertical: true,
    asNavFor: '#product-main-img',
		responsive: [{
        breakpoint: 991,
        settings: {
					vertical: false,
					arrows: false,
					dots: true,
        }
      },
    ]
  });

	// Product img zoom
	var zoomMainProduct = document.getElementById('product-main-img');
	if (zoomMainProduct) {
		$('#product-main-img .product-preview').zoom();
	}

	/////////////////////////////////////////

	// Input number
	$('.input-number').each(function() {
		var $this = $(this),
		$input = $this.find('input[type="number"]'),
		up = $this.find('.qty-up'),
		down = $this.find('.qty-down');

		down.on('click', function () {
			var value = parseInt($input.val()) - 1;
			value = value < 1 ? 1 : value;
			$input.val(value);
			$input.change();
			updatePriceSlider($this , value)
		})

		up.on('click', function () {
			var value = parseInt($input.val()) + 1;
			$input.val(value);
			$input.change();
			updatePriceSlider($this , value)
		})
	});

	var priceInputMax = document.getElementById('price-max'),
			priceInputMin = document.getElementById('price-min');

	if (priceInputMax) {
		priceInputMax.addEventListener('change', function(){
			updatePriceSlider($(this).parent() , this.value)
		});
	}

	if (priceInputMin) {
		priceInputMin.addEventListener('change', function(){
			updatePriceSlider($(this).parent() , this.value)
		});
	}

	function updatePriceSlider(elem , value) {
		if ( elem.hasClass('price-min') ) {
			console.log('min')
			priceSlider.noUiSlider.set([value, null]);
		} else if ( elem.hasClass('price-max')) {
			console.log('max')
			priceSlider.noUiSlider.set([null, value]);
		}
	}

	// Price Slider
	var priceSlider = document.getElementById('price-slider');
	if (priceSlider) {
		noUiSlider.create(priceSlider, {
			start: [0, 9999],
			connect: true,
			step: 1,
			range: {
				'min': 0,
				'max': 9999
			}
		});

		priceSlider.noUiSlider.on('update', function( values, handle ) {
			var value = values[handle];
			handle ? priceInputMax.value = value : priceInputMin.value = value
		});
	}


	/************************************************************\
	 					ONCLICK FUNCTIONS
	\************************************************************/
	$(".add-to-wishlist").click((e)=>{
		e.preventDefault();
		let product = $(e.target).closest(".product"),
			id = product.attr("data-id");
	});
	$("#responsive-nav .main-nav li, .search-btn").click((e)=>{
		$("#loading").show();
	});
	$(".view-product").click((e)=>{
		// if target inside .add-to-cart area, do nothing
		if ($(e.target).closest(".add-to-cart").length) {
			addToCart(e, 1);
			return;
		}
		if ($(e.target).closest(".product-btns").length && !$(e.target).closest(".quick-view").length)
			return;
		$("#loading").show();
		let product = $(e.target).closest(".view-product"),
			id = product.attr("data-id"),
			info = atob(product.attr("data-info"));
		let loc = window.location;
		if (id) {
			window.location.href = "/product" + info + "-" + id;
		}
	});
	function addToCart(e, qty) {
		let product = $(e.target).closest(".product"),
			id = product.attr("data-id");
		if (id) {
			$.ajax({
				url: "/cart/add",
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify({
					pdId: id,
					qty: qty
				}),
				success: (res)=>{
					let dia = $('#message-dialog');
					dia.find("#goCart").hide();
					if (res.status === 200) {
						let cartNum = $("#cart-count");
						cartNum.text(parseInt(cartNum.text()) + 1);
						cartNum.show();
						dia.find(".modal-body").text(res.message);
						dia.find("#goCart").show();
						dia.modal('show');
					}
					else if (res.status === 401) {
						dia.find(".modal-body").text(res.message);
						dia.modal('show');
						// wating for close modal and redirect to login page
						$("#loginA").show();
						dia.on('hidden.bs.modal',  (e) => {
							$("#loginA").hide();
						});
					}
					else {
						dia.find(".modal-body").text(res.message);
						dia.modal('show');
					}

				}
			});
		}
	}
	$(".page-link").click((e)=>{
		let target = $(e.target);
		if (target.parent().hasClass("active"))
			return;
		$("#loading").show();
		let page = target.data("page"),
			query = target.closest(".store-pagination").data("page");
		if (!query)
			query = "";
		query = atob(query);
		if (page)
			window.location.href = location.pathname +
				"?" + query + "&page=" + page;
	});

	/************************************************************\
	 					ONHOVER FUNCTIONS
	\************************************************************/
	$(".aside .product-widget .add-to-cart-btn").hover((e)=>{
		let product = e.target.closest(".product-widget"),
			// product name div box
			pN = $(product).find(".product-name"),
			atab = pN.find("a");

		let delta = parseInt(pN.css("width")) - parseInt(atab.css("width"));
		if (delta < 120)
			pN.css("visibility", "hidden");
	}, (e)=>{
		let pN = $(e.target.closest(".product-widget")).find(".product-name");
		pN.css("visibility", "visible");
	});


	window.addEventListener("beforeunload", ()=>{
		$("#loading").show();
	});
});

$(window).on("load", function() {
	$("#loading").hide();
});
function goBack() {
	window.history.back();
}