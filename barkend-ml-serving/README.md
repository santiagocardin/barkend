

    kubectl -n barkend port-forward svc/tfserving-example 8000:8000

    curl -X POST -H 'Content-Type: application/json'  -d '{ "data": { "ndarray": [[1,2,3]] } }' localhost:8000/api/v1.0/predictions

http://localhost:8000/api/v1.0/doc/seldon.json