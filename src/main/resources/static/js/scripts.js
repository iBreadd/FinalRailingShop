/*!
* Start Bootstrap - One Page Wonder v6.0.6 (https://startbootstrap.com/theme/one-page-wonder)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-one-page-wonder/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

const formContainer = document.getElementById('form-container');

function addForm() {
  const form = document.createElement('form');
  // add form fields to the new form element
  formContainer.appendChild(form);
}

formContainer.addEventListener('submit', (event) => {
  event.preventDefault();
  addForm();
});