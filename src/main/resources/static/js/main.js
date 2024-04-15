new Vue({
    el: '#faq',
    data: {
        faqItems: document.querySelectorAll('.faq-item')
    },

    methods: {
        faqItemsToggle: function (e) {
            var el = e.target;
            var item = el.closest('.faq-item');
            var answer = item.querySelector('p'); // Get the <p> element inside .faq-item

            if (answer.classList.contains('active')) {
                answer.classList.remove('active');
                answer.style.height = '0px';
            } else {
                answer.classList.add('active');
            }
        },
        displayAll: function () {
            console.log(this.faqItems);
            this.faqItems.forEach(function (item) {
                var answer = item.querySelector('p');
                answer.classList.add('active');
                answer.style.height = answer.scrollHeight + 'px';
            });
        }
    },
});