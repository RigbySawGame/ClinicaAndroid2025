<?php 
    $nombres = $_GET['nombres'];
    $apaterno = $_GET['apaterno'];
    $amaterno = $_GET['amaterno'];
    $edad = $_GET['edad'];
    $sexo = $_GET['sexo'];
    $altura = $_GET['altura'];
    $peso = $_GET['peso'];
    $detalle = $_GET['detalle'];

    require_once("funciones.php");
    $rpta = GuardarPaciente($nombres, $apaterno, $amaterno, $edad, $sexo, $altura, $peso, $detalle);

    echo $rpta;
?>