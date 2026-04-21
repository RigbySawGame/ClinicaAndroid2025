<?php
    require_once("funciones.php");
    $rpta = MostrarPaciente();
    echo json_encode($rpta);
?>