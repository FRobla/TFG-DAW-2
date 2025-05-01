import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarBusquedaComponent } from './navbar-busqueda.component';

describe('NavbarBusquedaComponent', () => {
  let component: NavbarBusquedaComponent;
  let fixture: ComponentFixture<NavbarBusquedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NavbarBusquedaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarBusquedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
