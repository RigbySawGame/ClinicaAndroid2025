<?php 
    $id = $_GET['id'];

    require_once("funciones.php");
    $rpta = EliminarPaciente($id);

    echo $rpta;
?>