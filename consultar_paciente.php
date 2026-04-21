<?php
    $id = $_GET['id'];

    require_once("funciones.php");
    $rpta = ConsultarPaciente($id);

    echo json_encode($rpta);
?>